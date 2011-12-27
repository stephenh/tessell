package org.tessell.model.properties;

import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasRuleTriggers {

  public HandlerRegistration addRuleTriggeredHandler(RuleTriggeredHandler handler);

  public HandlerRegistration addRuleUntriggeredHandler(RuleUntriggeredHandler handler);

}
