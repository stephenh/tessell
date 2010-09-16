package org.gwtmpv.model.commands;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;

import org.gwtmpv.dispatch.client.util.OutstandingDispatchAsync;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;
import org.gwtmpv.model.properties.BooleanProperty;

import com.google.gwt.user.client.rpc.AsyncCallback;

/** Maintains the state of a {@code gwt-dispatch}-style UiCommand. */
public abstract class DispatchUiCommand<A extends Action<R>, R extends Result> extends UiCommand implements AsyncCallback<R> {

  private final OutstandingDispatchAsync async;
  private final BooleanProperty active = booleanProperty("active", false);

  public DispatchUiCommand(OutstandingDispatchAsync async) {
    this.async = async;
  }

  @Override
  protected final void doExecute() {
    if (active.isTrue()) {
      return;
    }
    active.set(true);
    async.execute(createAction(), this);
  }

  @Override
  public void onSuccess(R result) {
    onResult(result);
    active.set(false);
  }

  @Override
  public void onFailure(Throwable caught) {
    // trust that caught is handled by someone listening for DispatchFailureEvents
    active.set(false);
  }

  protected abstract A createAction();

  protected abstract void onResult(R result);

  /** @return whether the call is currently active */
  public BooleanProperty getActive() {
    return active;
  }

}
