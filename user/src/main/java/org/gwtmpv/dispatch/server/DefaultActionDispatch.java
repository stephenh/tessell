package org.gwtmpv.dispatch.server;

import org.gwtmpv.dispatch.server.handlers.ActionHandler;
import org.gwtmpv.dispatch.server.handlers.ActionHandlerRegistry;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.ActionException;
import org.gwtmpv.dispatch.shared.Result;

/** Stock server-side implementation of {@link ActionDispatch}. */
public class DefaultActionDispatch implements ActionDispatch {

  protected final ActionHandlerRegistry handlers = new ActionHandlerRegistry();

  /** Executes {@code action} and ensures only {@link ActionException}s are thrown. */
  @Override
  public <A extends Action<R>, R extends Result> R execute(final A action, final ExecutionContext context) throws ActionException {
    try {
      return findHandler(action).execute(action, context);
    } catch (ActionException ae) {
      log(action, context, ae);
      throw ae; // allow ActionException subclasses to go back
    } catch (Exception e) {
      log(action, context, e);
      throw new ActionException("A server error occurred"); // don't leak the raw exception message
    }
  }

  /** Adds {@code handler}. */
  public void addHandler(final ActionHandler<?, ?> handler) {
    handlers.addHandler(handler);
  }

  /** @return the handler for {@code action} or throws {@code IllegalStateException} */
  protected <A extends Action<R>, R extends Result> ActionHandler<A, R> findHandler(final A action) {
    final ActionHandler<A, R> handler = getHandlerRegistry().findHandler(action);
    if (handler == null) {
      throw new IllegalStateException("No handler for " + action);
    }
    return handler;
  }

  /** Subclasses should override to provide logging. */
  protected void log(final Action<?> action, final ExecutionContext context, Exception e) {
  }

  protected ActionHandlerRegistry getHandlerRegistry() {
    return handlers;
  }

}
