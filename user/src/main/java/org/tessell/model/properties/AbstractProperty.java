package org.tessell.model.properties;

import static org.tessell.util.ObjectUtils.eq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;

/** Provides most of the validation/derived/etc. implementation guts of {@link Property}. */
public abstract class AbstractProperty<P, T extends AbstractProperty<P, T>> implements Property<P> {

  private static final Logger log = Logger.getLogger("org.tessell.model");
  private static List<Property<?>> implicitUpstream = null;
  // handlers
  private final EventBus handlers = new SimplerEventBus();
  // other properties that are validated off of our value
  protected final ArrayList<Property<?>> derived = new ArrayList<Property<?>>();
  // rules that validate against our value and fire against our handlers
  private final ArrayList<Rule> rules = new ArrayList<Rule>();
  // outstanding errors
  private final Map<Object, String> errors = new LinkedHashMap<Object, String>();
  // our wrapped value
  private final Value<P> value;
  // snapshot of the value for diff purposes (e.g. derived values)
  private P lastValue;
  // whether the user has touched this field on the screen yet
  private boolean touched;
  // the result of the last validate()
  private Valid valid;

  public AbstractProperty(final Value<P> value) {
    this.value = value;
    lastValue = copyLastValue(getWithUpstreamTracking());
    RuleHandler ruleHandler = new RuleHandler();
    addRuleTriggeredHandler(ruleHandler);
    addRuleUntriggeredHandler(ruleHandler);
  }

  @Override
  public P get() {
    if (implicitUpstream != null && !implicitUpstream.contains(this)) {
      // Some other derived property is having it's get() called, so it depends on us now
      implicitUpstream.add(this);
    }
    return getWithUpstreamTracking();
  }

  @Override
  public boolean isReadOnly() {
    return value.isReadOnly();
  }

  @Override
  public void set(final P value) {
    this.value.set(copyLastValue(value));
    if (!touched) {
      // even if unchanged, treat this as touching
      setTouched(true);
    } else {
      reassess();
    }
  }

  @Override
  public void reassess() {
    final P newValue = get();
    final P oldValue = lastValue;
    final boolean valueChanged = !eq(lastValue, newValue);
    if (valueChanged) {
      lastValue = copyLastValue(newValue);
    }

    // run validation before firing change so handlers see latest wasValid
    final Valid oldValid = valid;
    validate();
    final boolean validChanged = oldValid != valid;

    // only reassess derived if needed. this is somehwat odd, but we reassess
    // our derived properties before firing our own change event. this is so
    // that if someone listening to us is also going to check a derived
    // property's state, it would be good for them to be up to date
    if (valueChanged || validChanged) {
      for (final Property<?> other : derived) {
        other.reassess();
      }
    }

    if (valueChanged) {
      fireEvent(new PropertyChangedEvent<P>(this, oldValue, newValue));
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
  public <P1 extends Property<?>> P1 addDerived(final P1 other) {
    if (!derived.contains(other)) {
      derived.add(other);
      if (touched) {
        other.setTouched(touched);
      }
    }
    return other;
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  @Override
  public <T1> FormattedProperty<T1, P> formatted(final PropertyFormatter<P, T1> formatter) {
    return new FormattedProperty<T1, P>(this, formatter, null);
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

  @Override
  public void addRule(final Rule rule) {
    if (rules.contains(rule)) {
      return;
    }
    if (rule.isImportant()) {
      rules.add(0, rule);
    } else {
      rules.add(rule);
    }
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
  public HandlerRegistration addRuleTriggeredHandler(final RuleTriggeredHandler handler) {
    return addHandler(RuleTriggeredEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(final RuleUntriggeredHandler handler) {
    return addHandler(RuleUntriggeredEvent.getType(), handler);
  }

  @Override
  public boolean isTouched() {
    return touched;
  }

  @Override
  public void setTouched(final boolean touched) {
    if (this.touched == touched) {
      return;
    }
    this.touched = touched;
    for (final Property<?> other : derived) {
      other.setTouched(touched);
    }
    reassess();
  }

  @Override
  public Valid wasValid() {
    return valid;
  }

  public Value<P> getValue() {
    return value;
  }

  public T req() {
    new Required(this);
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

  protected abstract T getThis();

  protected <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
    return handlers.addHandlerToSource(type, this, handler);
  }

  /** Runs validation against our rules. */
  private void validate() {
    valid = Valid.YES; // start out valid
    for (final Rule rule : rules) {
      if (rule.validate() == Valid.YES) {
        rule.untriggerIfNeeded();
      } else {
        // only trigger the first invalid rule
        if (valid == Valid.YES) {
          if (isTouched()) {
            rule.triggerIfNeeded();
          } else {
            rule.untriggerIfNeeded();
          }
          valid = Valid.NO;
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
      // If a DerivedValue, turn on implicitUpstream, which will watch for any properties
      // we evaluate while calling value.get for lastValue purposes.
      List<Property<?>> oldUpstream = implicitUpstream;
      implicitUpstream = new ArrayList<Property<?>>();
      P tempValue = value.get();
      List<Property<?>> ourUpstream = new ArrayList<Property<?>>(implicitUpstream);
      implicitUpstream = oldUpstream;
      // Now continue with copyLastValue and then act upon ourUpstream
      for (Property<?> upstream : ourUpstream) {
        upstream.addDerived(this);
      }
      return tempValue;
    } else {
      return value.get();
    }
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

}
