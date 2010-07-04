package org.gwtmpv.dispatch.server;

import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

/**
 * Server-side execution of the action and returns the results.
 * 
 * @author David Peterson
 */
public interface ActionDispatch {

  /** Executes the specified action and returns the appropriate result. */
  <A extends Action<R>, R extends Result> R execute(A action, ExecutionContext context);

}
