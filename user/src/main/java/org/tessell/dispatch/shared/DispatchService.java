package org.tessell.dispatch.shared;

import org.tessell.dispatch.client.DispatchAsync;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The GWT-RPC interface used to execute actions.
 * 
 * This is the raw-type version of {@link DispatchAsync},
 * which adds tighter bounds on its Action/Result parameters.
 */
@RemoteServiceRelativePath("dispatch")
public interface DispatchService extends RemoteService {

  Result execute(String sessionId, Action<?> action) throws ActionException;

}
