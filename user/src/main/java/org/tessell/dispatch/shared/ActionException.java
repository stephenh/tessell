package org.tessell.dispatch.shared;

import org.tessell.dispatch.client.util.OutstandingDispatchAsync;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A specific exception to return from {@link DispatchService}.
 * 
 * GWT doesn't like random server-side {@code Exception}s making their way down to the client
 * (results in a serialization exception), so we convert all {@code Exception}s to an
 * {@code ActionException} (or an {@code ActionException} subclass).
 * 
 * Given without the checked exception in an RPC interface, you won't be able to use exceptions
 * anyway, your dispatch results should use return codes for anything but a truly
 * "reload-the-application-please" exception.
 *
 * (This is not really true--if you use only {@code ActionException} subclasses for your
 * error conditions, then you can use exceptions for both "expected" and "fatal" error
 * conditions, although you'd also have to tweak {@link OutstandingDispatchAsync} which
 * assumes any exception is fatal.)
 */
public class ActionException extends RuntimeException implements IsSerializable {

  private static final long serialVersionUID = 1L;

  protected ActionException() {
  }

  public ActionException(final String message) {
    super(message);
  }

}
