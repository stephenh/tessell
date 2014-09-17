package org.tessell.dispatch.server.servlet;

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
    ActionDispatch d = getActionDispatch();
    if (d == null) {
      throw new IllegalStateException("Null ActionDispatch, ensure the server started correctly");
    }
    try {
      beginAction(action);
      return d.execute(action, new ExecutionContext(getThreadLocalRequest(), getThreadLocalResponse(), sessionId));
    } catch (final ActionException ae) {
      // assume the user has already logged the ActionException appropriately
      throw ae;
    } catch (final Exception e) {
      logActionFailure(e);
      throw wrapInActionException(e);
    } finally {
      endAction(action);
    }
  }

  /** Allows subclasses to override exception logging. By default uses {@link GenericServlet#log}. */
  protected void logActionFailure(Exception e) {
    log(e.getMessage(), e);
  }

  /** Allows subclasses to log/MDC at the start of action handling. */
  protected void beginAction(Action<?> action) {
  }

  /** Allows subclasses to log/MDC at the end of action handling. */
  protected void endAction(Action<?> action) {
  }

  /** Allows subclasses to create their own "runtime exception" subclass of {@link ActionException}. */
  protected ActionException wrapInActionException(Exception e) {
    return new ActionException("A server error occured."); // don't leak the raw exception message
  }

  /** Method for subclasses to return their {@link ActionDispatch} class. */
  protected abstract ActionDispatch getActionDispatch();

}
