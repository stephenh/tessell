package org.tessell.dispatch.client;

/** An interface where the caller only cares about success results, and failures are handled by a separate, application-wide routine. */
public interface SuccessCallback<T> {

  void onSuccess(T result);

}
