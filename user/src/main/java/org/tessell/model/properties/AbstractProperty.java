package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.util.ObjectUtils.eq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Upstream.Capture;
import org.tessell.model.validation.Valid;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.validation.rules.Required;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;
import org.tessell.util.Inflector;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.*;
import com.google.gwt.event.shared.GwtEvent.Type;

/** Provides most of the validation/derived/etc. implementation guts of {@link Property}. */
public abstract class AbstractProperty<P, T extends AbstractProperty<P, T>> implements Property<P> {

  private static final Logger log = Logger.getLogger("org.tessell.model");
  // handlers
  private final EventBus handlers = new SimplerEventBus();
  // other properties that are validated off of our value
  protected final ArrayList<Downstream> downstream = new ArrayList<Downstream>();
  // rules that validate against our value and fire against our handlers
  private final ArrayList<Rule<? super P>> rules = new ArrayList<Rule<? super P>>();
  // outstanding errors
  private final Map<Object, String> errors = new LinkedHashMap<Object, String>();
  // our wrapped value
  private final Value<P> value;
  // snapshot of the value for diff purposes (e.g. derived values)
  private P lastValue;
  // what we should use for null
  private P defaultValue;
  // whether the user has touched this field on the screen yet
  private boolean touched;
  // whether this property is required
  private boolean required;
  // the result of the last validate()
  private Valid valid;
  // whether we're currently reassessing
  private boolean reassessing = false;
  // only used if this is a derived value
  private UpstreamState lastUpstream;

  public AbstractProperty(final Value<P> value) {
    this.value = value;
    lastValue = copyLastValue(getWithUpstreamTracking());
    RuleHandler ruleHandler = new RuleHandler();
    addRuleTriggeredHandler(ruleHandler);
    addRuleUntriggeredHandler(ruleHandler);
  }

  @Override
  public P get() {
    Upstream.addIfTracking(this);
    return getWithUpstreamTracking();
  }

  @Override
  public boolean isReadOnly() {
    return value.isReadOnly();
  }

  @Override
  public void set(final P value) {
    this.value.set(defaultIfNull(copyLastValue(value)));
    if (!touched && !reassessing) {
      // even if unchanged, treat this as touching
      setTouched(true);
    } else {
      reassess();
    }
  }

  @Override
  public void setInitialValue(final P value) {
    this.value.set(defaultIfNull(copyLastValue(value)));
    reassess();
  }

  @Override
  public void setDefaultValue(final P value) {
    this.defaultValue = value;
    setIfNull(value);
  }

  @Override
  public void reassess() {
    try {
      reassessing = true;

      P newValue = get();
      // watch for out-of-band changes, e.g. model.merge(newDto);
      if (newValue == null && defaultValue != null) {
        value.set(defaultValue);
        newValue = defaultValue;
      }
      final P oldValue = lastValue;
      final boolean valueChanged = !eq(lastValue, newValue);
      if (valueChanged) {
        lastValue = copyLastValue(newValue);
      }

      // run validation before firing change so handlers see latest wasValid
      final Valid oldValid = valid;
      validate();
      final boolean validChanged = oldValid != valid;

      // only reassess downstream if needed. this is somewhat odd, but we reassess
      // our downstream properties before firing our own change event. this is so
      // that if someone listening to us is also going to check a downstream
      // property's state, it would be good for them to be up to date
      if (valueChanged || validChanged) {
        for (final Downstream other : new ArrayList<Downstream>(downstream)) {
          other.property.reassess();
        }
      }

      if (valueChanged) {
        fireChanged(oldValue, newValue);
      }
    } finally {
      reassessing = false;
    }
  }

  /** Allow subclasses to deep copy values if needed. */
  protected P copyLastValue(P newValue) {
    return newValue;
  }

  @Override
  public HandlerRegistration addPropertyChangedHandler(final PropertyChangedHandler<P> handler) {
    return addHandler(PropertyChangedEvent.getType(), handler);
  }

  /** Track {@code other} as derived on us, so we'll forward changed/changing events to it. */
  @Override
  public <P1 extends Property<?>> P1 addDerived(final P1 other) {
    return addDerived(other, this, true);
  }

  /** Track {@code other} as derived on us, so we'll forward changed/changing events to it. */
  @Override
  public <P1 extends Property<?>> P1 addDerived(P1 other, Object token, boolean touch) {
    Downstream d = findDownstreamOrNull(other);
    if (d != null) {
      d.tokens.add(token);
      // upgrade an existing non-touch to touch
      if (!d.touch && touch) {
        d.touch = true;
        if (touched) {
          other.setTouched(touched);
        }
      }
    } else {
      d = new Downstream(other, touch);
      d.tokens.add(token);
      downstream.add(d);
      if (touched) {
        other.setTouched(touched);
      }
    }
    return other;
  }

  /** Remove {@code other} as derived on us. */
  @Override
  public <P1 extends Property<?>> P1 removeDerived(final P1 other) {
    return removeDerived(other, this);
  }

  @Override
  public <P1 extends Property<?>> P1 removeDerived(final P1 other, final Object token) {
    Downstream d = findDownstreamOrNull(other);
    if (d != null) {
      d.tokens.remove(token);
      if (d.tokens.size() == 0) {
        downstream.remove(d);
      }
    }
    return other;
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  @Override
  public <T1> FormattedProperty<T1, P> formatted(final PropertyFormatter<P, T1> formatter) {
    return new FormattedProperty<T1, P>(this, formatter);
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  @Override
  public <T1> FormattedProperty<T1, P> formatted(final String invalidMessage, final PropertyFormatter<P, T1> formatter) {
    return new FormattedProperty<T1, P>(this, formatter, invalidMessage);
  }

  /** @return a new derived property by applying {@code converter} to our value */
  @Override
  public <T1> Property<T1> as(final PropertyConverter<P, T1> converter) {
    return new ConvertedProperty<T1, P>(this, converter);
  }

  @Override
  public Property<String> asString() {
    return as(new PropertyConverter<P, String>() {
      public String to(P a) {
        return a.toString();
      }
    });
  }

  @Override
  public String toString() {
    return value.toString();
  }

  // fluent method of touch + valid
  public Valid touch() {
    setTouched(true);
    return wasValid();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addRule(final Rule<? super P> rule) {
    if (rules.contains(rule)) {
      return;
    }
    if (rule.isImportant()) {
      rules.add(0, rule);
    } else {
      rules.add(rule);
    }
    ((Rule<P>) rule).setProperty(this);
    reassess();
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    log.finest(this + " firing " + event);
    handlers.fireEventFromSource(event, this);
  }

  @Override
  public T depends(final Property<?>... upstream) {
    for (final Property<?> other : upstream) {
      other.addDerived(this);
    }
    return getThis();
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<P> handler) {
    // translate PropertyChangedEvents to ValueChangedEvents
    return addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        // need an inner class because ValueChangedEvent's cstr is protected
        handler.onValueChange(new ValueChangeEvent<P>(event.getNewValue()) {
        });
      }
    });
  }

  @Override
  public HandlerRegistration addRuleTriggeredHandler(final RuleTriggeredHandler handler) {
    return addHandler(RuleTriggeredEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(final RuleUntriggeredHandler handler) {
    return addHandler(RuleUntriggeredEvent.getType(), handler);
  }

  @Override
  public boolean isTouched() {
    Upstream.addIfTracking(this);
    return touched;
  }

  @Override
  public void setTouched(final boolean touched) {
    if (this.touched == touched) {
      return;
    }
    this.touched = touched;
    for (final Downstream other : new ArrayList<Downstream>(downstream)) {
      if (other.touch) {
        other.property.setTouched(touched);
      }
    }
    reassess();
  }

  @Override
  public boolean isRequired() {
    return required;
  }

  @Override
  public void setRequired(final boolean required) {
    this.required = required;
  }

  @Override
  public Valid wasValid() {
    Upstream.addIfTracking(this);
    return valid;
  }

  @Override
  public P getValue() {
    return get();
  }

  @Override
  public void setValue(P value) {
    set(value);
  }

  @Override
  public void setValue(P value, boolean fire) {
    // we always fire
    set(value);
  }

  public T req() {
    addRule(new Required());
    return getThis();
  }

  public T in(final PropertyGroup group) {
    group.add(this);
    return getThis();
  }

  @Override
  public String getName() {
    return Inflector.humanize(value.getName());
  }

  @Override
  public Map<Object, String> getErrors() {
    return errors;
  }

  @Override
  public void setIfNull(P value) {
    if (get() == null) {
      setInitialValue(value);
    }
  }

  @Override
  public Property<Boolean> is(final P value) {
    return is(value, null);
  }

  @Override
  public Property<Boolean> is(final P value, final P whenUnsetValue) {
    final BooleanProperty is = booleanProperty(getName() + "Is" + value);
    is.setInitialValue(eq(get(), value));
    final boolean[] changing = { false };
    // is -> this
    is.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        if (isReadOnly()) {
          changing[0] = true;
          is.set(eq(get(), value));
          changing[0] = false;
        } else if (event.getNewValue() != null && event.getNewValue()) {
          AbstractProperty.this.set(value);
        } else if (!changing[0]) {
          AbstractProperty.this.set(whenUnsetValue);
        }
      }
    });
    // this -> is
    addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(eq(get(), value));
        changing[0] = false;
      }
    });
    return is;
  }

  @Override
  public Property<Boolean> is(final Property<P> other) {
    return is(other, null);
  }

  @Override
  public Property<Boolean> is(final Property<P> other, final P whenUnsetValue) {
    final BooleanProperty is = booleanProperty(getName() + "Is" + value);
    is.setInitialValue(eq(get(), other.get()));
    final boolean[] changing = { false };
    // is -> this
    is.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        if (isReadOnly()) {
          changing[0] = true;
          is.set(eq(get(), other.get()));
          changing[0] = false;
        } else if (event.getNewValue() != null && event.getNewValue()) {
          AbstractProperty.this.set(other.get());
        } else if (!changing[0]) {
          AbstractProperty.this.set(whenUnsetValue);
        }
      }
    });
    // this -> is
    addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(eq(get(), other.get()));
        changing[0] = false;
      }
    });
    // other -> is
    other.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(eq(get(), other.get()));
        changing[0] = false;
      }
    });
    return is;
  }

  protected Value<P> getValueObject() {
    return value;
  }

  protected abstract T getThis();

  protected <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
    return handlers.addHandlerToSource(type, this, handler);
  }

  protected void fireChanged(P oldValue, P newValue) {
    fireEvent(new PropertyChangedEvent<P>(this, oldValue, newValue));
  }

  /** Runs validation against our rules. */
  private void validate() {
    valid = Valid.YES; // start out valid
    for (final Rule<? super P> rule : rules) {
      if (rule.validate() == Valid.YES) {
        rule.untriggerIfNeeded();
      } else {
        // only trigger the first invalid rule
        if (valid == Valid.YES) {
          valid = Valid.NO;
          if (isTouched()) {
            rule.triggerIfNeeded();
          } else {
            rule.untriggerIfNeeded();
          }
        } else {
          rule.untriggerIfNeeded();
        }
      }
    }
  }

  private P getWithUpstreamTracking() {
    // this logic should probably go in DerivedValue somehow, except that
    // it's only a value and does not know about it's parent property
    if (value instanceof DerivedValue) {
      if (lastUpstream == null) {
        lastUpstream = new UpstreamState(this, true);
      }
      Capture c = Upstream.start();
      P tempValue = value.get();
      lastUpstream.update(c.finish());
      return tempValue;
    } else {
      return value.get();
    }
  }

  private Downstream findDownstreamOrNull(Property<?> other) {
    for (Downstream downstream : this.downstream) {
      if (downstream.property == other) {
        return downstream;
      }
    }
    return null;
  }

  private P defaultIfNull(P value) {
    return (value == null) ? defaultValue : value;
  }

  /** Remembers rules fired against us. */
  private class RuleHandler implements RuleTriggeredHandler, RuleUntriggeredHandler {
    public void onUntrigger(RuleUntriggeredEvent event) {
      errors.remove(event.getKey());
    }

    public void onTrigger(RuleTriggeredEvent event) {
      errors.put(event.getKey(), event.getMessage());
    }
  }

  /** Wrapper for tracking downstream properties, plus whether we should touch them. */
  static class Downstream {
    final Property<?> property;
    // list of what objects (e.g. UpstreamState) requested this downstream
    final List<Object> tokens = new ArrayList<Object>();
    boolean touch;

    private Downstream(Property<?> property, boolean touch) {
      this.property = property;
      this.touch = touch;
    }
  }

}
