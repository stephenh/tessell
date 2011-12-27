package org.tessell.dispatch.client.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.tessell.dispatch.shared.Action;

@GenEvent
public class DispatchActionEventSpec {
  @Param(1)
  Action<?> action;
  @Param(2)
  String message;
}
