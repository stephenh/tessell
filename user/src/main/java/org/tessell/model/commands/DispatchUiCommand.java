package org.tessell.model.commands;

import static org.tessell.model.properties.NewProperty.booleanProperty;

import org.tessell.dispatch.client.events.DispatchUnhandledFailureEvent;
import org.tessell.dispatch.client.util.OutstandingDispatchAsync;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;
import org.tessell.model.properties.BooleanProperty;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Maintains the state of a {@code gwt-dispatch}-style UiCommand.
 *
 * Along with {@link UiCommand}'s {@code enabled} property, this adds an {@code active}
 * property that denotes whether a dispatch command has already fired an action and is
 * waiting for a result.
 *
 * This allows conditional action on the command's activeness, e.g. disabling buttons.
 */
public abstract class DispatchUiCommand<A extends Action<R>, R extends Result> extends UiCommand implements HasActive {

  private final OutstandingDispatchAsync async;
  private final BooleanProperty active = booleanProperty("active", false);
  private int highestActionIndex;
  private int highestResultIndex;
  private int currentActionIndex;
  protected R result;

  public DispatchUiCommand(OutstandingDispatchAsync async) {
    this.async = async;
  }

  @Override
  protected final void doExecute() {
    doExecute(createAction());
  }

  protected final void doExecute(final A action) {
    if (action != null) {
      active.set(true);
      final int thisActionIndex = ++highestActionIndex;
      // It would be nice to use a SuccessCallback, but we need to know
      // when the failure happened to toggle active back to false
      async.execute(action, new AsyncCallback<R>() {
        public void onSuccess(R r) {
          highestResultIndex = Math.max(highestResultIndex, thisActionIndex);
          currentActionIndex = thisActionIndex;
          result = r;
          onResult();
          if (thisActionIndex == highestActionIndex) {
            active.set(false);
          }
        }

        public void onFailure(Throwable caught) {
          highestResultIndex = Math.max(highestResultIndex, thisActionIndex);
          currentActionIndex = thisActionIndex;
          result = null;
          DispatchUiCommand.this.onFailure(caught);
          if (thisActionIndex == highestActionIndex) {
            active.set(false);
          }
        }
      });
    }
  }

  /** @return whether the command is currently active */
  public BooleanProperty active() {
    return active;
  }

  /** Implemented by subclasses to create the action object. */
  protected abstract A createAction();

  /** Implemented by subclasses to handle a successful result. */
  protected abstract void onResult();

  /** Fires a {@link DispatchUnhandledFailureEvent} on failures, can be overridden by subclasses if needed. */
  protected void onFailure(Throwable caught) {
    async.unhandledFailure(caught);
  }

  /** Allows subclasses to tell if the current action is stale. */
  protected boolean hasNewerActionBeenSent() {
    return currentActionIndex < highestActionIndex;
  }

  /** Allows subclasses to tell if the current result is stale. */
  protected boolean hasNewerResultBeenReceived() {
    return currentActionIndex < highestResultIndex;
  }

}
