package org.gwtmpv.dispatch.client.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.dispatch.shared.Action;

@GenEvent
public class DispatchActionEventSpec {
  Action<?> p1action;
  String p2message;
}
