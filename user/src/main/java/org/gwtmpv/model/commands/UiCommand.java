package org.gwtmpv.model.commands;

import static java.lang.Boolean.FALSE;
import static org.gwtmpv.model.properties.NewProperty.booleanProperty;

import java.util.ArrayList;

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
import com.google.gwt.event.shared.SimpleEventBus;

/** Codifies a UI command that may have rules triggered. */
public abstract class UiCommand implements HasRuleTriggers {

  private final BooleanProperty enabled = booleanProperty("enabled", true);
  private final EventBus handlers = new SimpleEventBus();
  private final ArrayList<String> errors = new ArrayList<String>();
  private final ArrayList<Property<Boolean>> onlyIf = new ArrayList<Property<Boolean>>();

  public void execute() {
    if (enabled.isTrue()) {
      for (Property<Boolean> p : onlyIf) {
        if (p.touch() == Valid.NO || FALSE.equals(p.get())) {
          return;
        }
      }
      clearErrors();
      doExecute();
    }
  }

  public void addOnlyIf(Property<Boolean> onlyIf) {
    this.onlyIf.add(onlyIf);
    // TODO onlyIf.addDerived()
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
