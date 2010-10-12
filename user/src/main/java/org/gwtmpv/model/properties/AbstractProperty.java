package org.gwtmpv.model.properties;

import static org.gwtmpv.util.ObjectUtils.eq;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;
import org.gwtmpv.model.validation.rules.Required;
import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.Value;
import org.gwtmpv.util.Inflector;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/** Provides most of the validation/derived/etc. implementation guts of {@link Property}. */
public abstract class AbstractProperty<P, T extends AbstractProperty<P, T>> implements Property<P> {

  // handlers
  protected final EventBus handlers = new SimpleEventBus();
  // other properties that are validated off of our value
  protected final ArrayList<Property<?>> derived = new ArrayList<Property<?>>();
  // rules that validate against our value and fire against our handlers
  private final ArrayList<Rule> rules = new ArrayList<Rule>();
  // our wrapped value
  private final Value<P> value;
  // snapshot of the value for diff purposes (e.g. derived values)
  protected P lastValue;
  // whether the user has touched this field on the screen yet
  private boolean touched;
  // the result of the last validate()
  private Valid valid;
  // has fired at least once
  private boolean hasFired;

  public AbstractProperty(final Value<P> value) {
    this.value = value;
    lastValue = get();
  }

  @Override
  public P get() {
    return value.get();
  }

  @Override
  public void set(final P value) {
    this.value.set(value);
    setTouched(true); // even if unchanged, treat this as touching
  }

  @Override
  public void setInitial(final P value) {
    this.value.set(value);
    setTouched(false);
  }

  @Override
  public void reassess() {
    final P newValue = get();
    final boolean changed = !eq(lastValue, newValue);
    lastValue = newValue;
    if (changed || !hasFired) {
      fireEvent(new PropertyChangedEvent<P>(this));
      hasFired = true;
    }

    validate();

    for (final Property<?> other : derived) {
      other.reassess();
    }
  }

  @Override
  public HandlerRegistration addPropertyChangedHandler(final PropertyChangedHandler<P> handler) {
    return addHandler(PropertyChangedEvent.getType(), handler);
  }

  /** Track {@code other} as derived on us, so we'll forward changed/changing events to it. */
  public <P1 extends Property<?>> P1 addDerived(final P1 other) {
    derived.add(other);
    return other;
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  public <T1> Property<T1> formatted(final PropertyFormatter<P, T1> formatter) {
    return addDerived(new BasicProperty<T1>(new DerivedValue<T1>("formatted " + getName()) {
      @Override
      public T1 get() {
        return formatter.format(AbstractProperty.this.get());
      }
    }));
  }

  @Override
  public String toString() {
    return value.getName();
  }

  // fluent method of touch + valid
  public Valid touch() {
    setTouched(true);
    return wasValid();
  }

  @Override
  public void addRule(final Rule rule) {
    rules.add(rule);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    handlers.fireEvent(event);
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
    return Inflector.humanize(toString());
  }

  @Override
  public void pullInitial() {
    setInitial(get());
  }

  protected abstract T getThis();

  protected <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
    return handlers.addHandlerToSource(type, this, handler);
  }

  /** Runs validation against our rules. */
  private void validate() {
    valid = Valid.YES; // start out valid
    for (final Rule rule : rules) {
      if (rule.validate() == Valid.NO) {
        valid = Valid.NO;
      }
    }
  }

}
