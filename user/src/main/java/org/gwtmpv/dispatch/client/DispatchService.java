package org.gwtmpv.dispatch.client;

import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.ActionException;
import org.gwtmpv.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dispatch")
public interface DispatchService extends RemoteService {

  Result execute(String sessionId, Action<?> action) throws ActionException;

}
