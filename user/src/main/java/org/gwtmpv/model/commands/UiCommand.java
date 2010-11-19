package org.gwtmpv.model.commands;

import static java.lang.Boolean.FALSE;
import static org.gwtmpv.model.properties.NewProperty.booleanProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.properties.HasRuleTriggers;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.SimplerEventBus;

/** Codifies a UI command that may have rules triggered. */
public abstract class UiCommand implements HasRuleTriggers {

  private final BooleanProperty enabled = booleanProperty("enabled", true);
  private final EventBus handlers = new SimplerEventBus();
  private final Map<String, HasHandlers> errors = new HashMap<String, HasHandlers>();
  private final ArrayList<Property<Boolean>> onlyIf = new ArrayList<Property<Boolean>>();

  public void execute() {
    if (enabled.isTrue()) {
      clearErrors();
      for (Property<Boolean> p : onlyIf) {
        if (p.touch() == Valid.NO || FALSE.equals(p.get())) {
          return;
        }
      }
      doExecute();
    }
  }

  public void addOnlyIf(Property<Boolean> onlyIf) {
    this.onlyIf.add(onlyIf);
    // TODO onlyIf.addDerived()
  }

  protected abstract void doExecute();

  /** Fires an error message against this command's handlers. */
  public void error(String message) {
    error(handlers, message);
  }

  /** Fires an error message against the {@code errorTarget}'s handlers. */
  public void error(HasHandlers errorTarget, String message) {
    if (errors.containsKey(message)) {
      return;
    }
    errors.put(message, errorTarget);
    errorTarget.fireEvent(new RuleTriggeredEvent(message, message, new Boolean[] { false }));
  }

  protected void clearErrors() {
    for (Map.Entry<String, HasHandlers> e : errors.entrySet()) {
      e.getValue().fireEvent(new RuleUntriggeredEvent(e.getKey(), e.getKey()));
    }
    errors.clear();
  }

  @Override
  public HandlerRegistration addRuleTriggeredHandler(RuleTriggeredHandler handler) {
    return handlers.addHandler(RuleTriggeredEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(RuleUntriggeredHandler handler) {
    return handlers.addHandler(RuleUntriggeredEvent.getType(), handler);
  }

}
