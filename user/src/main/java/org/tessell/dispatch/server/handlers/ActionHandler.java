package org.tessell.dispatch.server.handlers;

import org.tessell.dispatch.server.ExecutionContext;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

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

  /** Allows certain handlers to opt-in to skipping for CSRF (e.g. for login handlers before a token is available). */
  boolean skipCSRFCheck();

}
