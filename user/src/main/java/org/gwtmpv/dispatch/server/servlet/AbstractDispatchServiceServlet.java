package org.gwtmpv.dispatch.server.servlet;

import static org.gwtmpv.util.ObjectUtils.eq;

import org.gwtmpv.dispatch.client.DispatchService;
import org.gwtmpv.dispatch.server.ActionDispatch;
import org.gwtmpv.dispatch.server.ExecutionContext;
import org.gwtmpv.dispatch.server.SessionIdValidator;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.ActionException;
import org.gwtmpv.dispatch.shared.Result;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractDispatchServiceServlet extends RemoteServiceServlet implements DispatchService {

  private static final long serialVersionUID = 1L;

  public Result execute(final String sessionId, final Action<?> action) throws ActionException {
    try {
      final ExecutionContext context = new ExecutionContext(getThreadLocalRequest(), getThreadLocalResponse());
      if (!eq(sessionId, getSessionValidator().get(context))) {
        throw new IllegalStateException("Invalid session");
      }
      return getActionDispatch().execute(action, context);
    } catch (final Exception e) {
      throw new ActionException(e.getMessage());
    }
  }

  protected abstract SessionIdValidator getSessionValidator();

  protected abstract ActionDispatch getActionDispatch();

}