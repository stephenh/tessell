package org.gwtmpv.model.properties;

import org.gwtmpv.model.validation.events.RuleTriggeredEvent.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent.RuleUntriggeredHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasRuleTriggers {

  public HandlerRegistration addRuleTriggeredHandler(RuleTriggeredHandler handler);

  public HandlerRegistration addRuleUntriggeredHandler(RuleUntriggeredHandler handler);

}
