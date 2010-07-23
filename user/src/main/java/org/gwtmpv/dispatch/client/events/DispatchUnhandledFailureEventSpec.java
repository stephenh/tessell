package org.gwtmpv.dispatch.client.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.dispatch.shared.Action;

@GenEvent
public class DispatchUnhandledFailureEventSpec {
  @Param(1)
  Action<?> action;
  @Param(2)
  Throwable throwable;
  @Param(3)
  String message;
}
