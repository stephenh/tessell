package org.tessell.model.validation.rules;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tessell.model.properties.Property;
import org.tessell.model.properties.Upstream;
import org.tessell.model.properties.Upstream.Capture;
import org.tessell.model.properties.UpstreamState;
import org.tessell.model.validation.Valid;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.values.Value;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;

/**
 * A base class with most of the common {@link Rule} functionality implemented
 *
 * @param T
 *          the value of the property for this rule
 * @param U
 *          the subclass for the {@link #getThis()} hack
 */
public abstract class AbstractRule<T> implements Rule<T> {

  private static final Logger log = Logger.getLogger("org.tessell.model");
  // handlers
  protected final EventBus handlers = new SimplerEventBus();
  // List of properties that must be true for this rule to run.
  private final ArrayList<Value<Boolean>> onlyIf = new ArrayList<Value<Boolean>>();
  // set by AbstractProperty.addRule
  protected Property<T> property;
  // The error message to show the user
  protected String message;
  // Whether this rule has been invalid and fired a RuleTriggeredEvent
  private boolean triggered = false;
  // only used if this is a derived value
  private UpstreamState lastUpstream;

  protected AbstractRule(String message) {
    this.message = message;
  }

  protected abstract Valid isValid();

  @Override
  public final Valid validate() {
    if (lastUpstream == null) {
      lastUpstream = new UpstreamState(property, false);
    }
    Capture c = Upstream.start();
    Valid v = doValidate();
    lastUpstream.update(c.finish());
    return v;
  }

  @Override
  public void setProperty(Property<T> property) {
    this.property = property;
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
  @Override
  public void onlyIf(final Value<Boolean> other) {
    this.onlyIf.add(other);
    if (property != null) {
      property.reassess();
    }
  }

  public void triggerIfNeeded(Valid validationResult) {
    if (!triggered && message != null) { // custom rules (PropertyGroup's) may not have explicit error messages
      boolean dontDisplay = validationResult == Valid.PENDING ? true : false;
      fireEvent(new RuleTriggeredEvent(this, message, new Boolean[] { dontDisplay }));
      triggered = true;
    }
  }

  public void untriggerIfNeeded() {
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
    // after all of our handlers have seen event, delegate it up.
    property.fireEvent(event);
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

  private Valid doValidate() {
    if (onlyIfSaysToSkip()) {
      return Valid.TRUE;
    }
    return this.isValid();
  }

}
