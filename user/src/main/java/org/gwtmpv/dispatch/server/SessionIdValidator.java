package org.gwtmpv.dispatch.server;

/** Interface for checking the session id on the server-side for XSRF. */
public interface SessionIdValidator {

  /** @return the user's session id given the {@code context} */
  String get(ExecutionContext context);

}
