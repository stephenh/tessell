package org.tessell.dispatch.server;

/** Interface for checking the session id on the server-side for XSRF. */
public interface SessionIdValidator {

  /** @return the user's secure session id given the {@code context} */
  String getToken(ExecutionContext context);

  /** Creates a new secure session id on the user's {@code context}, if one does not already exist. */
  void setTokenIfNeeded(ExecutionContext context);

}
