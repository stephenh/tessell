package org.tessell.dispatch.client.events;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

@GenEvent
public class DispatchResultEventSpec {
  @Param(1)
  Action<?> action;
  @Param(2)
  Result resultValue;
  @Param(3)
  String message;
}
