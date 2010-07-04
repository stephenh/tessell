package org.gwtmpv.dispatch.client.util;

import java.util.ArrayList;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.dispatch.client.DispatchAsync;
import org.gwtmpv.dispatch.client.events.DispatchActionEvent;
import org.gwtmpv.dispatch.client.events.DispatchFailureEvent;
import org.gwtmpv.dispatch.client.events.DispatchResultEvent;
import org.gwtmpv.dispatch.client.events.DispatchUnhandledFailureEvent;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;
import org.gwtmpv.util.SuccessCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

/** Fires events outstanding dispatch calls so tests can know when to continue. */
public class OutstandingDispatchAsync implements DispatchAsync {

  protected final EventBus eventBus;
  protected final DispatchAsync realDispatch;
  protected final ArrayList<Action<?>> outstanding = new ArrayList<Action<?>>();

  public OutstandingDispatchAsync(final EventBus eventBus, final DispatchAsync realDispatch) {
    this.eventBus = eventBus;
    this.realDispatch = realDispatch;
  }

  @Override
  public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
    execute(action, callback, null);
  }

  /**
   * Executes {@code action} and defers failure handling from the caller.
   * 
   * @param action
   *          the action
   * @param success
   *          the success-only callback, {@link DispatchUnhandledFailureEvent} will be fired on failure
   */
  public <A extends Action<R>, R extends Result> void execute(final A action, final SuccessCallback<R> success) {
    execute(action, success, null);
  }

  /**
   * Executes {@code action}, defers failure handling from the caller, with an in-progress {@code message}.
   * 
   * @param action
   *          the action
   * @param success
   *          the success-only callback, {@link DispatchUnhandledFailureEvent} will be fired on failure
   * @param message
   *          the in-progress message to include in the {@link DispatchActionEvent}/{@link DispatchResultEvent} events
   */
  public <A extends Action<R>, R extends Result> void execute(final A action, final SuccessCallback<R> success,
                                                              final String message) {
    execute(action, new AsyncCallback<R>() {
      public void onSuccess(final R result) {
        success.onSuccess(result);
      }

      public void onFailure(final Throwable caught) {
        eventBus.fireEvent(new DispatchUnhandledFailureEvent(action, caught, message));
      }
    }, message);
  }

  /**
   * Executes {@code action} with an in-progress {@code message}.
   * 
   * @param action
   *          the action
   * @param callback
   *          the callback for both success and failure (no {@link DispatchUnhandledFailureEvent} will be fired)
   * @param message
   *          the in-progress message to include in the {@link DispatchActionEvent}/{@link DispatchResultEvent} events
   */
  public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback,
                                                              final String message) {
    outstanding.add(action);
    eventBus.fireEvent(new DispatchActionEvent(action, message));
    realDispatch.execute(action, new AsyncCallback<R>() {
      public void onSuccess(final R result) {
        outstanding.remove(action);
        eventBus.fireEvent(new DispatchResultEvent(action, result, message));
        callback.onSuccess(result);
      }

      public void onFailure(final Throwable caught) {
        outstanding.remove(action);
        eventBus.fireEvent(new DispatchFailureEvent(action, caught, message));
        callback.onFailure(caught);
      }
    });
  }

  /** @return whether there are action calls that have not returned from the server for {@code actionType} */
  public <A extends Action<R>, R extends Result> boolean hasOutstanding(final Class<A> actionType) {
    for (final Action<?> action : outstanding) {
      if (action.getClass().equals(actionType)) {
        return true;
      }
    }
    return false;
  }

}