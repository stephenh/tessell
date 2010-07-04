package org.gwtmpv.dispatch.client;

import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DispatchServiceAsync {

  void execute(String sessionId, Action<?> action, AsyncCallback<Result> callback);

}
