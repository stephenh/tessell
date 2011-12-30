package org.tessell.model.validation.events;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent(methodName = "onTrigger", gwtEvent = true)
public class RuleTriggeredEventSpec {
  @Param(1)
  Object key;
  @Param(2)
  String message;
  @Param(3)
  Boolean[] displayed;
}
