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
public abstract class DispatchUiCommand<A extends Action<R>, R extends Result> extends UiCommand {

  private final OutstandingDispatchAsync async;
  private final BooleanProperty active = booleanProperty("active", false);

  public DispatchUiCommand(OutstandingDispatchAsync async) {
    this.async = async;
  }

  /**
   * Executes the UI command, first triggering any "only if" validation,
   * and then calling {@code doExecute} if they all pass.
   *
   * @throws IllegalStateException if the command is disabled
   * @throws IllegalStateException if the command is already active
   */
  @Override
  public void execute() {
    if (active.isTrue()) {
      throw new IllegalStateException("Command is already executing");
    }
    super.execute();
  }

  @Override
  protected final void doExecute() {
    A action = createAction();
    if (action != null) {
      active.set(true);
      // It would be nice to use a SuccessCallback, but we need to know
      // when the failure happened to toggle active back to false
      async.execute(action, new AsyncCallback<R>() {
        public void onSuccess(R result) {
          onResult(result);
          active.set(false);
        }

        public void onFailure(Throwable caught) {
          DispatchUiCommand.this.onFailure(caught);
          active.set(false);
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
  protected abstract void onResult(R result);

  /** Fires a {@link DispatchUnhandledFailureEvent} on failures, can be overridden by subclasses if needed. */
  protected void onFailure(Throwable caught) {
    async.unhandledFailure(caught);
  }

}
