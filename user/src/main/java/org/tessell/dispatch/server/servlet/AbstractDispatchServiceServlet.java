package org.tessell.dispatch.server.servlet;

import static org.tessell.util.ObjectUtils.eq;

import javax.servlet.GenericServlet;

import org.tessell.dispatch.server.ActionDispatch;
import org.tessell.dispatch.server.ExecutionContext;
import org.tessell.dispatch.server.SessionIdValidator;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.ActionException;
import org.tessell.dispatch.shared.DispatchService;
import org.tessell.dispatch.shared.Result;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Provides a basic {@link DispatchService} implementation that defers to subclasses
 * for the {@link SessionIdValidator} and {@link ActionDispatch} instances.
 */
public abstract class AbstractDispatchServiceServlet extends RemoteServiceServlet implements DispatchService {

  private static final long serialVersionUID = 1L;

  @Override
  public Result execute(final String sessionId, final Action<?> action) throws ActionException {
    try {
      final ExecutionContext context = new ExecutionContext(getThreadLocalRequest(), getThreadLocalResponse());
      if (getSessionValidator() != null && !eq(sessionId, getSessionValidator().get(context))) {
        throw invalidSession(context);
      }
      ActionDispatch d = getActionDispatch();
      if (d == null) {
        throw new IllegalStateException("Null ActionDispatch, ensure the server started correctly");
      }
      return d.execute(action, context);
    } catch (final ActionException ae) {
      // assume the user has already logged the ActionException appropriately
      throw ae;
    } catch (final Exception e) {
      log(e);
      throw wrapInActionException(e);
    }
  }

  /** Allows subclasses to override exception logging. By default uses {@link GenericServlet#log}. */
  protected void log(Exception e) {
    log(e.getMessage(), e);
  }

  /** Allows subclasses to create their own "runtime exception" subclass of {@link ActionException}. */
  protected ActionException wrapInActionException(Exception e) {
    return new ActionException("A server error occured."); // don't leak the raw exception message
  }

  /** Allows subclasses to create their own invalid session subclasses of {@link ActionException}. */
  protected Exception invalidSession(ExecutionContext context) {
    return new IllegalStateException("Invalid session");
  }

  protected abstract SessionIdValidator getSessionValidator();

  protected abstract ActionDispatch getActionDispatch();

}
