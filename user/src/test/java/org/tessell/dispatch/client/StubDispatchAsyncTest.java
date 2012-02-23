package org.tessell.dispatch.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class StubDispatchAsyncTest {

  private final StubDispatchAsync async = new StubDispatchAsync();
  private final StubCallback callback = new StubCallback();

  @Test
  public void callbackIsCalled() {
    async.execute(new GetTestAction("a"), callback);

    final GetTestResult result = new GetTestResult();
    async.getCallback(GetTestAction.class).onSuccess(result);
    assertThat(callback.result, is(result));
  }

  @Test
  public void successCannotBeCalledTwice() {
    async.execute(new GetTestAction("a"), callback);
    async.getCallback(GetTestAction.class).onSuccess(new GetTestResult());
    try {
      async.getCallback(GetTestAction.class).onSuccess(new GetTestResult());
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("GetTestAction[a] has already been called back"));
    }
  }

  @Test
  public void failureCannotBeCalledTwice() {
    async.execute(new GetTestAction("a"), callback);
    async.getCallback(GetTestAction.class).onFailure(null);
    try {
      async.getCallback(GetTestAction.class).onFailure(null);
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("GetTestAction[a] has already been called back"));
    }
  }

  @Test
  public void successCannotBeCalledOutOfOrder() {
    async.execute(new GetTestAction("a"), callback);
    async.execute(new GetTestAction("a"), callback);
    try {
      async.getCallback(GetTestAction.class, 1).onSuccess(null);
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Call for GetTestAction[a] cannot return before call for GetTestAction[a]"));
    }
  }

  @Test
  public void successCanExplicitlyBeOutOfOrder() {
    async.execute(new GetTestAction("a"), callback);
    async.execute(new GetTestAction("a"), callback);
    async.getCallback(GetTestAction.class, 1).onSuccessOutOfOrder(null);
    async.getCallback(GetTestAction.class, 0).onSuccessOutOfOrder(null);
  }

  private class StubCallback implements AsyncCallback<GetTestResult> {
    private GetTestResult result;

    @Override
    public void onSuccess(GetTestResult result) {
      this.result = result;
    }

    @Override
    public void onFailure(Throwable t) {
    }
  }

}
