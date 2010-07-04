package org.gwtmpv.dispatch.server.handlers;

import java.util.Map;

import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

public class DefaultActionHandlerRegistry implements ActionHandlerRegistry {

  private final Map<Class<?>, ActionHandler<?, ?>> handlers = new java.util.HashMap<Class<?>, ActionHandler<?, ?>>(100);

  public void addHandler(final ActionHandler<?, ?> handler) {
    handlers.put(handler.getActionType(), handler);
  }

  @SuppressWarnings("unchecked")
  public <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(final A action) {
    return (ActionHandler<A, R>) handlers.get(action.getClass());
  }

}
