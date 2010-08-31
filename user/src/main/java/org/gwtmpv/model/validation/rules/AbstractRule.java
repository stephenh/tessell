package org.gwtmpv.model.validation.rules;

import java.util.ArrayList;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent.RuleUntriggeredHandler;
import org.gwtmpv.model.values.Value;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A base class with most of the common {@link Rule} functionality implemented
 * 
 * @param T
 *          the value of the property for this rule
 * @param U
 *          the subclass for the {@link #getThis()} hack
 */
public abstract class AbstractRule<T, U extends AbstractRule<T, U>> implements Rule {

  // handlers
  protected final HandlerManager handlers = new HandlerManager(this);
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
  public final Valid validate(final boolean propertyIsAlreadyInvalid) {
    if (onlyIfSaysToSkip()) {
      untriggerIfNeeded();
      return Valid.YES;
    }

    // TODO if unchanged (and not derived?), return the last result
    // TODO prevent recursion by marking ourselves already validating
    if (propertyIsAlreadyInvalid) {
      untriggerIfNeeded();
      return Valid.NO;
    }

    // we used to always vote DEFER if not touched--allow voting YES

    final Valid valid = this.isValid();

    if (valid == Valid.YES) {
      untriggerIfNeeded();
    } else {
      if (property.isTouched()) {
        triggerIfNeeded();
      } else {
        untriggerIfNeeded();
        return Valid.NO;
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
    this.onlyIf.add(other.getValue());
    other.addDownstream(this.property);
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

  private void fireEvent(final GwtEvent<?> event) {
    System.out.println(event.toString());
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

}
