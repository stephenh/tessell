package org.tessell.model.validation.events;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent(methodName = "onUntrigger", gwtEvent = true)
public class RuleUntriggeredEventSpec {
  @Param(1)
  Object key;
  @Param(2)
  String message;
}
