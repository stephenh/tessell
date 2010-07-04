package org.gwtmpv.model.validation.events;

import org.gwtmpv.GenEvent;

@GenEvent(methodName = "onTrigger")
public class RuleTriggeredEventSpec {
  Object p1key;
  String p2message;
  Boolean[] p3displayed;
}
