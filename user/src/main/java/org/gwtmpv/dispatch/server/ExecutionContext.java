package org.gwtmpv.dispatch.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gwtmpv.dispatch.server.handlers.ActionHandler;

/** ExecutionContext instances are passed to {@link ActionHandler}s. */
public class ExecutionContext {

  private final HttpServletRequest request;
  private final HttpServletResponse response;

  public ExecutionContext(final HttpServletRequest request, final HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

}
