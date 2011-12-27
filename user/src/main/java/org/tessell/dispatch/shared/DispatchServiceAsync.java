package org.tessell.dispatch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/** The client-side interface for {@link DispatchService}. */
public interface DispatchServiceAsync {

  void execute(String sessionId, Action<?> action, AsyncCallback<Result> callback);

}
