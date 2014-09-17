package org.tessell.dispatch.server;

import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.ActionException;
import org.tessell.dispatch.shared.Result;

/**
 * Server-side execution of the action and returns the results.
 *
 * @author David Peterson
 */
public interface ActionDispatch {

  /**
   * Executes the specified action and returns the appropriate result.
   *
   * @throws ActionException because it's best to not let arbitrary RuntimeExceptions attempt to be serialized
   */
  <A extends Action<R>, R extends Result> R execute(A action, ExecutionContext context) throws ActionException;

  /** @return whether we should skip the CSRF check for the given action. */
  boolean skipCSRFCheck(Action<?> action);
}
