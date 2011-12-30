package org.tessell.dispatch.client.events;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.dispatch.shared.Action;

@GenEvent
public class DispatchActionEventSpec {
  @Param(1)
  Action<?> action;
  @Param(2)
  String message;
}
