package org.gwtmpv.model.validation.rules;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;
import org.gwtmpv.model.values.Value;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * A base class with most of the common {@link Rule} functionality implemented
 * 
 * @param T
 *          the value of the property for this rule
 * @param U
 *          the subclass for the {@link #getThis()} hack
 */
public abstract class AbstractRule<T, U extends AbstractRule<T, U>> implements Rule {

  private static final Logger log = Logger.getLogger("org.gwtmpv.model");
  // handlers
  protected final EventBus handlers = new SimpleEventBus();
  // The property that wraps our value
  protected final Property<T> property;
  // List of properties that must be true for this rule to run.
  private final ArrayList<Value<Boolean>> onlyIf = new ArrayList<Value<Boolean>>();
  // The error message to show the user
  protected String message;
  // Whether this rule has been invalid and fired a RuleTriggeredEvent
  private boolean triggered = false;

  protected AbstractRule(final Property<T> property, final String message) {
    this.property = property;
    this.message = message;
    if (property != null) {
      property.addRule(this);
    }
  }

  protected abstract Valid isValid();

  protected abstract U getThis();

  @Override
  public final Valid validate() {
    if (onlyIfSaysToSkip()) {
      untriggerIfNeeded();
      return Valid.YES;
    }

    // TODO if unchanged (and not derived?), return the last result
    // TODO prevent recursion by marking ourselves already validating
    boolean propertyIsAlreadyInvalid = (property == null) ? false : property.wasValid() == Valid.NO;
    if (propertyIsAlreadyInvalid) {
      untriggerIfNeeded();
      return Valid.NO;
    }

    final Valid valid = this.isValid();

    if (valid == Valid.YES) {
      untriggerIfNeeded();
    } else {
      if (property.isTouched()) {
        triggerIfNeeded();
      } else {
        untriggerIfNeeded();
      }
    }
    return valid;
  }

  @Override
  public HandlerRegistration addRuleTriggeredHandler(final RuleTriggeredHandler handler) {
    return handlers.addHandler(RuleTriggeredEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(final RuleUntriggeredHandler handler) {
    return handlers.addHandler(RuleUntriggeredEvent.getType(), handler);
  }

  /** Only run this rule if {@code other} is true */
  public U onlyIf(final Property<Boolean> other) {
    other.addPropertyChangedHandler(new OnOnlyIfPropertyChanged());
    this.onlyIf.add(other);
    return getThis();
  }

  private void triggerIfNeeded() {
    if (!triggered && message != null) { // custom rules (PropertyGroup's) may not have explicit error messages
      fireEvent(new RuleTriggeredEvent(this, message, new Boolean[] { false }));
      triggered = true;
    }
  }

  private void untriggerIfNeeded() {
    if (triggered) {
      fireEvent(new RuleUntriggeredEvent(this, message));
      triggered = false;
    }
  }

  @Override
  public String toString() {
    return message;
  }

  @Override
  public boolean isImportant() {
    return false;
  }

  private void fireEvent(final GwtEvent<?> event) {
    log.log(Level.FINEST, this + " firing " + event);
    handlers.fireEvent(event);
    if (property != null) {
      property.fireEvent(event);
    }
  }

  // change this to push down too
  private boolean onlyIfSaysToSkip() {
    for (final Value<Boolean> only : onlyIf) {
      if (only.get() != null && !only.get().booleanValue()) {
        return true;
      }
    }
    return false;
  }

  /** Called when one of our guard properties changes. */
  private final class OnOnlyIfPropertyChanged implements PropertyChangedHandler<Boolean> {
    public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
      // call reassess so that Property#validate sets valid is set back
      // to YES, and we can know if we're the first invalid rule or not
      if (property != null) {
        property.reassess();
      } else {
        validate();
      }
    }
  }

}
