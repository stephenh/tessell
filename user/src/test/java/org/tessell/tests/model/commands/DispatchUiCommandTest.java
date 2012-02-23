package org.tessell.tests.model.commands;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

    async.getCalls().get(0).onSuccess(null);
    assertThat(command.active().get(), is(FALSE));
  }

  @Test
  public void activeIsTrueThenFalseWhenRpcFails() {
    DummyUiCommand command = new DummyUiCommand(async);
    assertThat(command.active().get(), is(FALSE));

    command.execute();
    assertThat(command.active().get(), is(TRUE));

    async.getCalls().get(0).onFailure(null);
    assertThat(command.active().get(), is(FALSE));
  }

  @Test
  public void allowsReExecution() {
    DummyUiCommand command = new DummyUiCommand(async);
    // initial execute is find
    command.execute();
    assertThat(command.createActionCalls, is(1));

    command.execute();
    assertThat(command.createActionCalls, is(2));

    // when the first response finally comes back
    async.getCalls().get(0).onSuccess(null);
    // it calls onResult
    assertThat(command.onResultCalls, is(1));
    // but it could tell it was a stale result
    assertThat(command.wasStale, is(true));

    // note that the command is still active
    assertThat(command.active().get(), is(true));

    // and when the second response comes back
    async.getCalls().get(1).onSuccess(null);
    // it also calls onResult
    assertThat(command.onResultCalls, is(2));
    // and this result was not stale
    assertThat(command.wasStale, is(false));
  }

  /** Fails depending on the instance variable {@code fail}. */
  private final class DummyUiCommand extends DispatchUiCommand<Action<Result>, Result> {
    private int createActionCalls = 0;
    private int onResultCalls = 0;
    private boolean wasStale = false;

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
    protected void onResult() {
      wasStale = isStale();
      onResultCalls++;
    }
  }

}
