package org.gwtmpv.dispatch.shared;

import org.gwtmpv.dispatch.client.DispatchService;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Checked exception to return from {@link DispatchService}.
 * 
 * GWT doesn't like random server-side RuntimeExceptions making their way down to the client (results in a serialization
 * exception), so we wrap all Exceptions in an ActionException.
 * 
 * Given without the checked exception in an RPC interface, you won't be able to use exceptions anyway, your dispatch
 * results should use return codes for anything but a truly "reload-the-application-please" exception.
 */
public class ActionException extends RuntimeException implements IsSerializable {

  private static final long serialVersionUID = 1L;

  protected ActionException() {
  }

  public ActionException(final String message) {
    super(message);
  }

}
