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
public abstract class AbstractRule<T, U extends AbstractRule<T, U>> implements Rule {

  private static final Logger log = Logger.getLogger("org.tessell.model");
  protected final Property<T> property;
  // handlers
  protected final EventBus handlers = new SimplerEventBus();
  // List of properties that must be true for this rule to run.
  private final ArrayList<Value<Boolean>> onlyIf = new ArrayList<Value<Boolean>>();
  // The error message to show the user
  protected String message;
  // Whether this rule has been invalid and fired a RuleTriggeredEvent
  private boolean triggered = false;
  // only used if this is a derived value
  private UpstreamState lastUpstream;

  protected AbstractRule(final Property<T> property, final String message) {
    this.message = message;
    this.property = property;
    if (property != null) {
      property.addRule(this);
    }
  }

  protected abstract Valid isValid();

  protected abstract U getThis();

  @Override
  public final Valid validate() {
    if (onlyIfSaysToSkip()) {
      return Valid.YES;
    }
    if (this instanceof Custom) {
      if (lastUpstream == null) {
        lastUpstream = new UpstreamState(property);
      }
      Capture c = Upstream.start();
      Valid v = this.isValid();
      lastUpstream.update(c.finish());
      return v;
    } else {
      return this.isValid();
    }
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
  public U onlyIf(final Value<Boolean> other) {
    this.onlyIf.add(other);
    return getThis();
  }

  public void triggerIfNeeded() {
    if (!triggered && message != null) { // custom rules (PropertyGroup's) may not have explicit error messages
      fireEvent(new RuleTriggeredEvent(this, message, new Boolean[] { false }));
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

}
