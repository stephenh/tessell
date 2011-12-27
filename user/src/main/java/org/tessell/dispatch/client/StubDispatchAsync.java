package org.tessell.dispatch.client;

import java.util.ArrayList;
import java.util.List;

import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

/** A stub implementation of {@link DispatchAsync}. */
public class StubDispatchAsync implements DispatchAsync {

  private final List<ExecuteCall<?, ?>> calls = new ArrayList<ExecuteCall<?, ?>>();

  @Override
  public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
    calls.add(new ExecuteCall<A, R>(action, callback));
  }

  /** @return all calls for assertions */
  public List<ExecuteCall<?, ?>> getCalls() {
    return calls;
  }

  /** @return all actions for assertions */
  public List<Action<?>> getActions() {
    final List<Action<?>> actions = new ArrayList<Action<?>>();
    for (final ExecuteCall<?, ?> call : calls) {
      actions.add(call.action);
    }
    return actions;
  }

  /** @return all calls for {@link actionType} for assertions */
  @SuppressWarnings("unchecked")
  public <A extends Action<R>, R extends Result> List<ExecuteCall<A, R>> getCalls(final Class<A> actionType) {
    final List<ExecuteCall<A, R>> matches = new ArrayList<ExecuteCall<A, R>>();
    for (final ExecuteCall<?, ?> call : calls) {
      if (actionType.equals(call.action.getClass())) {
        matches.add((ExecuteCall<A, R>) call);
      }
    }
    return matches;
  }

  /** @return the {@link AsyncCallback} for {@code actionType} {@code index} */
  public <A extends Action<R>, R extends Result> AsyncCallback<R> getCallback(final Class<A> actionType, final int index) {
    return getCalls(actionType).get(index).callback;
  }

  /** @return the last {@link AsyncCallback} for {@code actionType} */
  public <A extends Action<R>, R extends Result> AsyncCallback<R> getCallback(final Class<A> actionType) {
    final List<ExecuteCall<A, R>> calls = getCalls(actionType);
    if (calls.size() == 0) {
      throw new IllegalStateException("No calls for " + actionType);
    }
    return calls.get(calls.size() - 1).callback;
  }

  /** @return the {@link Action} for {@code actionType} {@code index} */
  public <A extends Action<R>, R extends Result> A getAction(final Class<A> actionType, final int index) {
    return getCalls(actionType).get(index).action;
  }

  /** @return the list {@link Action} for {@code actionType} */
  public <A extends Action<R>, R extends Result> A getAction(final Class<A> actionType) {
    final List<ExecuteCall<A, R>> calls = getCalls(actionType);
    if (calls.size() == 0) {
      throw new IllegalStateException("No calls have been made for " + actionType);
    }
    return calls.get(calls.size() - 1).action;
  }

  /** @return all {@link Action}s for {@code actionType} */
  public <A extends Action<R>, R extends Result> List<A> getActions(final Class<A> actionType) {
    final List<A> actions = new ArrayList<A>();
    for (final ExecuteCall<A, R> call : getCalls(actionType)) {
      actions.add(call.action);
    }
    return actions;
  }

  /** A DTO to track the calls the stub has been asked to make. */
  public class ExecuteCall<A extends Action<R>, R extends Result> {
    public final A action;
    public final AsyncCallback<R> callback;
    public boolean outstanding = true;

    public ExecuteCall(final A action, final AsyncCallback<R> callback) {
      this.action = action;
      this.callback = new AsyncCallback<R>() {
        public void onSuccess(final R result) {
          ensureOutstanding();
          callback.onSuccess(result);
        }

        public void onFailure(final Throwable caught) {
          ensureOutstanding();
          callback.onFailure(caught);
        }

        private void ensureOutstanding() {
          if (!outstanding) {
            throw new IllegalStateException(action + " has already been called back");
          }
          for (final ExecuteCall<?, ?> call : calls) {
            if (call == ExecuteCall.this) {
              break;
            }
            if (call.outstanding) {
              throw new IllegalStateException("Call for " + action + " cannot return before call for " + call.action);
            }
          }
          outstanding = false;
        }
      };
    }
  }

}
