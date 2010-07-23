package org.gwtmpv.model.validation.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;

@GenEvent(methodName = "onUntrigger")
public class RuleUntriggeredEventSpec {
  @Param(1)
  Object key;
  @Param(2)
  String message;
}
