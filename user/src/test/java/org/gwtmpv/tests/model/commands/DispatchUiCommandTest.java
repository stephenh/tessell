package org.gwtmpv.tests.model.commands;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.gwtmpv.dispatch.client.util.OutstandingDispatchAsync;
import org.gwtmpv.dispatch.client.util.StubOutstandingDispatchAsync;
import org.gwtmpv.dispatch.shared.Action;
import org.gwtmpv.dispatch.shared.Result;
import org.gwtmpv.model.commands.DispatchUiCommand;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class DispatchUiCommandTest extends AbstractRuleTest {

  final StubOutstandingDispatchAsync async = new StubOutstandingDispatchAsync();

  @Test
  public void activeIsTrueThenFalseWhenRpcIsSuccessful() {
    DummyUiCommand command = new DummyUiCommand(async);
    assertThat(command.getActive().get(), is(FALSE));

    command.execute();
    assertThat(command.getActive().get(), is(TRUE));

    async.getCalls().get(0).callback.onSuccess(null);
    assertThat(command.getActive().get(), is(FALSE));
  }

  @Test
  public void activeIsTrueThenFalseWhenRpcFails() {
    DummyUiCommand command = new DummyUiCommand(async);
    assertThat(command.getActive().get(), is(FALSE));

    command.execute();
    assertThat(command.getActive().get(), is(TRUE));

    async.getCalls().get(0).callback.onFailure(null);
    assertThat(command.getActive().get(), is(FALSE));
  }

  /** Fails depending on the instance variable {@code fail}. */
  private final class DummyUiCommand extends DispatchUiCommand<Action<Result>, Result> {
    public DummyUiCommand(OutstandingDispatchAsync async) {
      super(async);
    }

    @Override
    protected Action<Result> createAction() {
      return null;
    }

    @Override
    protected void onResult(Result result) {
    }
  }

}
