package org.tessell.dispatch.client;

import org.tessell.dispatch.server.ActionDispatch;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This is an asynchronous equivalent of the {@link ActionDispatch} interface on the server side.
 * 
 * @author David Peterson
 */
public interface DispatchAsync {

  <A extends Action<R>, R extends Result> void execute(A action, AsyncCallback<R> callback);

}
