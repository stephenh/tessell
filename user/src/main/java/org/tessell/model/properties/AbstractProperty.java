package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.util.ObjectUtils.eq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Upstream.Capture;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.validation.rules.Required;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.validation.rules.Static;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;
import org.tessell.util.Inflector;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.*;
import com.google.gwt.event.shared.GwtEvent.Type;

/** Provides most of the validation/derived/etc. implementation guts of {@link Property}. */
public abstract class AbstractProperty<P, T extends AbstractProperty<P, T>> extends AbstractAbstractProperty<P> {

  private static int outstandingSetInitials = 0;
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
  private Value<P> value;
  // snapshot of the value for diff purposes (e.g. derived values)
  private P lastValue;
  // what we should use for null
  private P defaultValue;
  // whether the user has touched this field on the screen yet
  private boolean touched;
  // whether this property is required
  private boolean required;
  // the result of the last validate()
  private boolean valid = true;
  // whether we're currently reassessing
  private boolean reassessing = false;
  // only used if this is a derived value
  private UpstreamState lastUpstream;
  // only used if showing a temporary error
  private Static temporaryRule = null;
  // only used if someone calls .valid()
  private Property<Boolean> validProperty;

  protected AbstractProperty() {
    // the subclass should call initialize as soon as possible
  }

  public AbstractProperty(final Value<P> value) {
    initializeValue(value);
  }

  /**
   * Basically the constructor, but separate so that {@link DerivedProperty}
   * can pass a DerivedValue anonymous class that refers to itself.
   */
  protected void initializeValue(final Value<P> value) {
    this.value = value;
    lastValue = copyLastValue(getWithUpstreamTracking());
    RuleHandler ruleHandler = new RuleHandler();
    addRuleTriggeredHandler(ruleHandler);
    addRuleUntriggeredHandler(ruleHandler);
    // Fixes people calling NewProperty.booleanProperty(otherBooleanProperty).not(); I'm
    // not entirely sure this is a good idea, but it passes the test.
    if (value instanceof Property) {
      ((Property<?>) value).addDerived(this);
    }
  }

  @SuppressWarnings("unchecked")
  public void setTemporaryError(String temporaryErrorMessage) {
    // Automatically touch so that the temporary error actually shows up
    setTouched(true);
    if (temporaryRule == null) {
      temporaryRule = new Static(temporaryErrorMessage);
      temporaryRule.setProperty((Property<Object>) this);
      rules.add(0, temporaryRule);
      temporaryRule.set(false);
    } else {
      // bounce the rule to retrigger it with the new message
      temporaryRule.set(true);
      temporaryRule.setMessage(temporaryErrorMessage);
      temporaryRule.set(false);
    }
  }

  public void clearTemporaryError() {
    clearTemporaryError(true);
  }

  @Override
  public HandlerRegistration addPropertyChangedHandler(final PropertyChangedHandler<P> handler) {
    return addHandler(PropertyChangedEvent.getType(), handler);
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
    this.value.set(copyLastValue(value));
    if (!touched && !reassessing && !isWithinASetInitial()) {
      // even if unchanged, treat this as touching
      setTouched(true);
    } else {
      reassess();
    }
  }

  @Override
  public void setInitialValue(final P value) {
    try {
      outstandingSetInitials++;
      this.value.set(defaultIfNull(copyLastValue(value)));
      reassess();
    } finally {
      outstandingSetInitials--;
    }
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
        lastValue = newValue; // so that we detect/fire change
        newValue = defaultValue;
      }
      final P oldValue = lastValue;
      final boolean valueChanged = !eq(lastValue, newValue);
      if (valueChanged) {
        lastValue = copyLastValue(newValue);
      }

      // run validation before firing change so handlers see latest wasValid
      final boolean oldValid = valid;
      validate();
      final boolean validChanged = oldValid != valid;
      if (validProperty != null) {
        validProperty.set(valid);
      }

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
        clearTemporaryError(false);
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

  /**
   * Track {@code other} as derived on us, so we'll forward changed/changing events to it.
   *
   * This is somewhat esoteric, but if you have property A and property B, B can depend on A
   * (be downstream/"derived") for (at least) two reasons:
   *
   * 1. Because B's value is inherently based on A
   * 2. Because B has a validation rule that is based on A
   *
   * For the 1st case, if A becomes touched, we also mark B as touched (so we pass percolateTouch=true).
   *
   * However, for the 2nd case, if A becomes touched, but B isn't really based on A, but instead just uses it
   * for a validation rule, then we don't want to mark B as touched (so we pass percolateTouch=false).
   *
   * @param other the property that depends on us
   * @param token a token that can be used to revoke the derivation (see {@link #removeDerived(Property, Object)}
   * @param percolateTouch whether we should percolate our touched state to {@code other}
   */
  @Override
  public <P1 extends Property<?>> P1 addDerived(P1 other, Object token, boolean percolateTouch) {
    Downstream d = findDownstreamOrNull(other);
    if (d != null) {
      d.tokens.add(token);
      // upgrade an existing non-touch to touch
      if (!d.touch && percolateTouch) {
        d.touch = true;
        if (touched) {
          other.setTouched(touched);
        }
      }
    } else {
      d = new Downstream(other, percolateTouch);
      d.tokens.add(token);
      downstream.add(d);
      if (percolateTouch && touched) {
        other.setTouched(touched);
      }
    }
    return other;
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

  @Override
  public String toString() {
    return value.toString();
  }

  // fluent method of touch + valid
  public boolean touch() {
    setTouched(true);
    return isValid();
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
    if (log.isLoggable(Level.FINEST)) {
      log.finest(this + " firing " + event);
    }
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
  public HandlerRegistration nowAndOnChange(final PropertyValueHandler<P> handler) {
    HandlerRegistration hr = addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        handler.onValue(event.getNewValue());
      }
    });
    handler.onValue(get());
    return hr;
  }

  @Override
  public boolean isTouched() {
    Upstream.addIfTracking(this);
    return touched;
  }

  @Override
  public void setTouched(final boolean touched) {
    clearTemporaryError(false);
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
  public boolean isValid() {
    Upstream.addIfTracking(this);
    return valid;
  }

  @Override
  public Property<Boolean> valid() {
    if (validProperty == null) {
      validProperty = booleanProperty(value.getName() + ".valid", valid);
    }
    return validProperty;
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
    valid = true; // start out valid
    for (final Rule<? super P> rule : rules) {
      if (rule.validate()) {
        rule.untriggerIfNeeded();
      } else {
        // only trigger the first invalid rule
        if (valid) {
          valid = false;
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

  private void clearTemporaryError(boolean resetTouched) {
    if (temporaryRule != null && !temporaryRule.isValid()) {
      temporaryRule.set(true);
    }
    if (resetTouched && touched) {
      setTouched(false);
    }
  }

  private static boolean isWithinASetInitial() {
    return outstandingSetInitials > 0;
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
