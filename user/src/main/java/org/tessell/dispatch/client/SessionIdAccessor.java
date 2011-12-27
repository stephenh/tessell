package org.tessell.dispatch.client;

/** Provides client-side access to the session id for dropping in to dispatch calls. */
public interface SessionIdAccessor {

  /** @return The current session id. */
  String getSessionId();

}
