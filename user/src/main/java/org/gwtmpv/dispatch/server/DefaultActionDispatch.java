package org.gwtmpv.dispatch.server;

import org.gwtmpv.dispatch.server.handlers.ActionHandler;
import org.gwtmpv.dispatch.server.handlers.ActionHandlerRegistry;
import org.gwtmpv.dispatch.server.handlers.DefaultActionHandlerRegistry;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

public class DefaultActionDispatch implements ActionDispatch {

  protected final DefaultActionHandlerRegistry handlers = new DefaultActionHandlerRegistry();

  @Override
  public <A extends Action<R>, R extends Result> R execute(final A action, final ExecutionContext context) {
    return findHandler(action).execute(action, context);
  }

  public void addHandler(final ActionHandler<?, ?> handler) {
    handlers.addHandler(handler);
  }

  protected <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(final A action) {
    final ActionHandler<A, R> handler = getHandlerRegistry().findHandler(action);
    if (handler == null) {
      throw new IllegalStateException("No handler for " + action);
    }
    return handler;
  }

  protected ActionHandlerRegistry getHandlerRegistry() {
    return handlers;
  }

}
