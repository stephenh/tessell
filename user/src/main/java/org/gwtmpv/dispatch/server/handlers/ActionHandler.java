package org.gwtmpv.dispatch.server.handlers;

import org.gwtmpv.dispatch.server.ExecutionContext;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

/**
 * Interface for handling a specific {@link Action} class {@code A} on the server.
 * 
 * @author David Peterson
 */
public interface ActionHandler<A extends Action<R>, R extends Result> {

  /** @return The type of {@link Action} supported by this handler. */
  Class<A> getActionType();

  /** Handles the specified action. */
  R execute(A action, ExecutionContext context);

}
