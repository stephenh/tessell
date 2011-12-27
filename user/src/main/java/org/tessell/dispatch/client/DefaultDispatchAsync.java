package org.tessell.dispatch.client;

import static org.tessell.util.StringUtils.substringAfterLast;

import org.tessell.dispatch.server.ActionDispatch;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.DispatchService;
import org.tessell.dispatch.shared.DispatchServiceAsync;
import org.tessell.dispatch.shared.Result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * This class is the default implementation of {@link DispatchAsync}, which is essentially the client-side access to the
 * {@link ActionDispatch} class on the server-side.
 * 
 * @author David Peterson
 */
public class DefaultDispatchAsync implements DispatchAsync {

  private static final DispatchServiceAsync realService = GWT.create(DispatchService.class);
  private static final String baseUrl = ((ServiceDefTarget) realService).getServiceEntryPoint() + "/";
  private final SessionIdAccessor sessionIdAccessor;

  public DefaultDispatchAsync(final SessionIdAccessor sessionIdAccessor) {
    this.sessionIdAccessor = sessionIdAccessor;
  }

  @Override
  public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
    final String sessionId = sessionIdAccessor == null ? null : sessionIdAccessor.getSessionId();
    // Append action class name as extra path info
    // http://turbomanage.wordpress.com/2010/03/19/adding-info-to-dispatch-url-for-logs/
    ((ServiceDefTarget) realService).setServiceEntryPoint(baseUrl + substringAfterLast(action.getClass().getName(), "."));
    realService.execute(sessionId, action, new AsyncCallback<Result>() {
      @SuppressWarnings("unchecked")
      public void onSuccess(final Result result) {
        DefaultDispatchAsync.this.onSuccess(action, (R) result, callback);
      }

      public void onFailure(final Throwable caught) {
        DefaultDispatchAsync.this.onFailure(action, caught, callback);
      }
    });
  }

  protected <A extends Action<R>, R extends Result> void onFailure(final A action, final Throwable caught, final AsyncCallback<R> callback) {
    callback.onFailure(caught);
  }

  protected <A extends Action<R>, R extends Result> void onSuccess(final A action, final R result, final AsyncCallback<R> callback) {
    callback.onSuccess(result);
  }

}
