package org.gwtmpv.dispatch.client;

import org.gwtmpv.dispatch.server.ActionDispatch;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This is an asynchronous equivalent of the {@link ActionDispatch} interface on the server side.
 * 
 * @author David Peterson
 */
public interface DispatchAsync {

  <A extends Action<R>, R extends Result> void execute(A action, AsyncCallback<R> callback);

}
