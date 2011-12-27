package org.tessell.tests.model.commands;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.tessell.dispatch.client.util.OutstandingDispatchAsync;
import org.tessell.dispatch.client.util.StubOutstandingDispatchAsync;
import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;
import org.tessell.model.commands.DispatchUiCommand;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class DispatchUiCommandTest extends AbstractRuleTest {

  final StubOutstandingDispatchAsync async = new StubOutstandingDispatchAsync();

  @Test
  public void activeIsTrueThenFalseWhenRpcIsSuccessful() {
    DummyUiCommand command = new DummyUiCommand(async);
    assertThat(command.active().get(), is(FALSE));

    command.execute();
    assertThat(command.active().get(), is(TRUE));

    async.getCalls().get(0).callback.onSuccess(null);
    assertThat(command.active().get(), is(FALSE));
  }

  @Test
  public void activeIsTrueThenFalseWhenRpcFails() {
    DummyUiCommand command = new DummyUiCommand(async);
    assertThat(command.active().get(), is(FALSE));

    command.execute();
    assertThat(command.active().get(), is(TRUE));

    async.getCalls().get(0).callback.onFailure(null);
    assertThat(command.active().get(), is(FALSE));
  }

  @Test
  public void executeFailsWhenAlreadyActive() {
    DummyUiCommand command = new DummyUiCommand(async);
    // initial execute is find
    command.execute();
    assertThat(command.createActionCalls, is(1));

    try {
      command.execute();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Command is already executing"));
      assertThat(command.createActionCalls, is(1));
    }

    // when the call finally comes back
    async.getCalls().get(0).callback.onFailure(null);
    // now we can execute again
    command.execute();
    assertThat(command.createActionCalls, is(2));
  }

  /** Fails depending on the instance variable {@code fail}. */
  private final class DummyUiCommand extends DispatchUiCommand<Action<Result>, Result> {
    private int createActionCalls = 0;

    public DummyUiCommand(OutstandingDispatchAsync async) {
      super(async);
    }

    @Override
    protected Action<Result> createAction() {
      createActionCalls++;
      return new Action<Result>() {
      };
    }

    @Override
    protected void onResult(Result result) {
    }
  }

}
