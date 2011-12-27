package org.tessell.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.tessell.dispatch.client.SuccessCallback;
import org.tessell.dispatch.client.util.OutstandingDispatchAsync;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

/**
 * @param A
 *          the action
 * @param R
 *          the result
 * @param D
 *          your derived result
 */
public abstract class ActionCache<A extends Action<R>, R extends Result, D> {

  private final OutstandingDispatchAsync async;
  private final HashMap<A, ResultHandler> results = new HashMap<A, ResultHandler>();

  public ActionCache(final OutstandingDispatchAsync async) {
    this.async = async;
  }

  public void execute(final A action) {
    execute(action, null);
  }

  public abstract D derive(R result);

  /** Executes <code>A</code> once, queueing any <code>onSuccess</code> until it arrives. */
  public void execute(final A action, final SuccessCallback<D> onSuccess) {
    ResultHandler h = results.get(action);
    if (h == null) {
      h = new ResultHandler(action);
      results.put(action, h);
    }
    h.execute(onSuccess);
  }

  /** Clears all cached results. */
  public void reset() {
    results.clear();
  }

  /** For given action, calls and caches its derived result. */
  private class ResultHandler implements SuccessCallback<R> {
    private final A action;
    private final ArrayList<SuccessCallback<D>> onSuccess = new ArrayList<SuccessCallback<D>>();
    private D derived;
    private boolean calling;

    private ResultHandler(final A action) {
      this.action = action;
    }

    private void execute(final SuccessCallback<D> onSuccess) {
      if (derived != null) {
        if (onSuccess != null) {
          onSuccess.onSuccess(derived);
        }
      } else if (calling) {
        if (onSuccess != null) {
          this.onSuccess.add(onSuccess);
        }
      } else {
        if (onSuccess != null) {
          this.onSuccess.add(onSuccess);
        }
        calling = true;
        async.execute(action, this);
      }
    }

    public void onSuccess(final R result) {
      derived = derive(result);
      for (final SuccessCallback<D> s : onSuccess) {
        s.onSuccess(derived);
      }
      onSuccess.clear();
    }
  }
}
