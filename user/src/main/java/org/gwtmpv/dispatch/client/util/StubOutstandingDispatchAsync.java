package org.gwtmpv.dispatch.client.util;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.dispatch.client.util.StubDispatchAsync.ExecuteCall;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unifies the {@link StubDispatchAsync} methods (via delegation) with the {@link OutstandingDispatchAsync} methods (Via
 * inheritance) so tests have a single type to use for both sets.
 */
public class StubOutstandingDispatchAsync extends OutstandingDispatchAsync {

  private final StubDispatchAsync realStub;

  public StubOutstandingDispatchAsync(final EventBus eventBus, final StubDispatchAsync realStub) {
    super(eventBus, realStub);
    this.realStub = realStub;
  }

  public ArrayList<Action<?>> getOutstanding() {
    return outstanding;
  }

  /** @return all calls for assertions */
  public List<ExecuteCall<?, ?>> getCalls() {
    return realStub.getCalls();
  }

  /** @return all actions for assertions */
  public List<Action<?>> getActions() {
    return realStub.getActions();
  }

  /** @return all calls for {@link actionType} for assertions */
  public <A extends Action<R>, R extends Result> List<ExecuteCall<A, R>> getCalls(final Class<A> actionType) {
    return realStub.getCalls(actionType);
  }

  /** @return the {@link AsyncCallback} for {@code actionType} {@code index} */
  public <A extends Action<R>, R extends Result> AsyncCallback<R> getCallback(final Class<A> actionType, final int index) {
    return realStub.getCallback(actionType, index);
  }

  /** @return the {@link AsyncCallback} for the last {@code actionType} */
  public <A extends Action<R>, R extends Result> AsyncCallback<R> getCallback(final Class<A> actionType) {
    return realStub.getCallback(actionType);
  }

  /** @return the {@link Action} for {@code actionType} {@code index} */
  public <A extends Action<R>, R extends Result> A getAction(final Class<A> actionType, final int index) {
    return realStub.getAction(actionType, index);
  }

  /** @return the {@link Action} for the last {@code actionType} */
  public <A extends Action<R>, R extends Result> A getAction(final Class<A> actionType) {
    return realStub.getAction(actionType);
  }

  /** @return all {@link Action}s for {@code actionType} */
  public <A extends Action<R>, R extends Result> List<A> getActions(final Class<A> actionType) {
    return realStub.getActions(actionType);
  }

}
