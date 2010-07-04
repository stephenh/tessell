package org.gwtmpv.dispatch.server.handlers;

import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

public interface ActionHandlerRegistry {

  /** @return the handler for {@code action} or {@code null} if none is available. */
  public <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(A action);

}
