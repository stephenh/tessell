package org.tessell.dispatch.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tessell.dispatch.server.handlers.ActionHandler;

/** ExecutionContext instances are passed to {@link ActionHandler}s. */
public class ExecutionContext {

  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final String sessionId;

  public ExecutionContext(final HttpServletRequest request, final HttpServletResponse response, final String sessionId) {
    this.request = request;
    this.response = response;
    this.sessionId = sessionId;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public String getSessionId() {
    return sessionId;
  }

}
