package org.tessell.dispatch.server;

import java.util.HashMap;
import java.util.Map;

import org.tessell.dispatch.server.handlers.ActionHandler;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

/** Stock server-side implementation of {@link ActionDispatch}. */
public class DefaultActionDispatch implements ActionDispatch {

  private final Map<Class<?>, ActionHandler<?, ?>> handlers = new HashMap<Class<?>, ActionHandler<?, ?>>(100);

  /** Executes {@code action}. */
  @Override
  public <A extends Action<R>, R extends Result> R execute(final A action, final ExecutionContext context) {
    return findHandler(action).execute(action, context);
  }

  /** Adds {@code handler}. */
  public void addHandler(final ActionHandler<?, ?> handler) {
    handlers.put(handler.getActionType(), handler);
  }

  @Override
  public boolean skipCSRFCheck(Action<?> action) {
    return findHandler(action).skipCSRFCheck();
  }

  /** @return the handler for {@code action} or throws {@code IllegalStateException} */
  protected <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(final A action) {
    @SuppressWarnings("unchecked")
    final ActionHandler<A, R> handler = (ActionHandler<A, R>) handlers.get(action.getClass());
    if (handler == null) {
      throw new IllegalStateException("No handler for " + action);
    }
    return handler;
  }

}
