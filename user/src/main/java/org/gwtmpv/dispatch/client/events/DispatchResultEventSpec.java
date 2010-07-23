package org.gwtmpv.dispatch.client.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

@GenEvent
public class DispatchResultEventSpec {
  @Param(1)
  Action<?> action;
  @Param(2)
  Result resultValue;
  @Param(3)
  String message;
}
