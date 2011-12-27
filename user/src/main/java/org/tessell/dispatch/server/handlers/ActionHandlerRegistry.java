package org.tessell.dispatch.server.handlers;

import java.util.HashMap;
import java.util.Map;

import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

/** Keeps a map of {@link Action} class to {@link ActionHandler} instance. */
public class ActionHandlerRegistry {

  private final Map<Class<?>, ActionHandler<?, ?>> handlers = new HashMap<Class<?>, ActionHandler<?, ?>>(100);

  @SuppressWarnings("unchecked")
  public <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(final A action) {
    return (ActionHandler<A, R>) handlers.get(action.getClass());
  }

  public void addHandler(final ActionHandler<?, ?> handler) {
    handlers.put(handler.getActionType(), handler);
  }

}
