package org.tessell.dispatch.shared;

import org.tessell.dispatch.server.ActionDispatch;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * An action represents a command sent to the {@link ActionDispatch}.
 * 
 * It has a specific result type which is returned if the action is successful.
 * 
 * @author David Peterson
 * @param <R>
 *          The {@link Result} type.
 */
public interface Action<R extends Result> extends IsSerializable {
}
