package org.gwtmpv.model.properties;

import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasRuleTriggers {

  public HandlerRegistration addRuleTriggeredHandler(RuleTriggeredHandler handler);

  public HandlerRegistration addRuleUntriggeredHandler(RuleUntriggeredHandler handler);

}
