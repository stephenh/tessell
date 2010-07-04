package org.gwtmpv.dispatch.client.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

@GenEvent
public class DispatchResultEventSpec {
  Action<?> p1action;
  Result p2resultValue;
  String p3message;
}
