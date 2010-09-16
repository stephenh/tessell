package org.gwtmpv.model.commands;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;

import java.util.ArrayList;

import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.properties.HasRuleTriggers;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent.RuleUntriggeredHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/** Codifies a UI command that may have rules triggered. */
public abstract class UiCommand implements HasRuleTriggers {

  private final BooleanProperty enabled = booleanProperty("enabled", true);
  private final HandlerManager handlers = new HandlerManager(this);
  private final ArrayList<String> errors = new ArrayList<String>();

  public void execute() {
    if (enabled.isTrue()) {
      clearErrors();
      doExecute();
    }
  }

  protected abstract void doExecute();

  protected void error(String message) {
    if (errors.contains(message)) {
      return;
    }
    errors.add(message);
    handlers.fireEvent(new RuleTriggeredEvent(message, message, new Boolean[] { false }));
  }

  protected void clearErrors() {
    for (String message : errors) {
      handlers.fireEvent(new RuleUntriggeredEvent(message, message));
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
