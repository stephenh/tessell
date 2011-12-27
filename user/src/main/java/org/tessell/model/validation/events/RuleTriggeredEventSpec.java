package org.tessell.model.validation.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;

@GenEvent(methodName = "onTrigger", gwtEvent = true)
public class RuleTriggeredEventSpec {
  @Param(1)
  Object key;
  @Param(2)
  String message;
  @Param(3)
  Boolean[] displayed;
}
