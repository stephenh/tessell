package org.tessell.dispatch.server;

import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.ActionException;
import org.tessell.dispatch.shared.Result;

/**
 * Dispatches actions by first checking the CSRF token.
 *
 * This prevents CSRF attacks as the payload value (e.g. in the request
 * body) must match the secure value (e.g. in the request headers), where
 * the secure value is only available to JavaScript on your domain.
 *
 * See {@link CookieSessionIdValidator} for a default implementation.
 */
public class SessionValidatorDispatch implements ActionDispatch {

  private final ActionDispatch delegate;
  private final SessionIdValidator validator;

  public SessionValidatorDispatch(SessionIdValidator validator, ActionDispatch delegate) {
    this.validator = validator;
    this.delegate = delegate;
  }

  @Override
  public <A extends Action<R>, R extends Result> R execute(A action, ExecutionContext context) throws ActionException {
    validator.setTokenIfNeeded(context);
    if (!delegate.skipCSRFCheck(action)) {
      String passedSessionId = context.getSessionId(); // from action payload
      String secureSessionId = validator.getToken(context); // from header
      if (secureSessionId == null || !secureSessionId.equals(passedSessionId)) {
        throw invalidSession(context);
      }
    }
    return delegate.execute(action, context);
  }

  @Override
  public boolean skipCSRFCheck(Action<?> action) {
    return delegate.skipCSRFCheck(action);
  }

  /** Allows subclasses to create their own invalid session subclasses of {@link ActionException}. */
  protected RuntimeException invalidSession(ExecutionContext context) {
    return new IllegalStateException("Invalid session");
  }

}
