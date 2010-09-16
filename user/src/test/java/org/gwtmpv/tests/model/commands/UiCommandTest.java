package org.gwtmpv.tests.model.commands;

import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class UiCommandTest extends AbstractRuleTest {

  final boolean[] fail = { true };

  @Test
  public void errorsAreClearedOnExecute() {
    UiCommand command = new DummyUiCommand();
    listenTo(command);

    command.execute();
    assertMessages("failed!");

    fail[0] = false;
    command.execute();
    assertMessages();
  }

  /** Fails depending on the instance variable {@code fail}. */
  private final class DummyUiCommand extends UiCommand {
    @Override
    protected void doExecute() {
      if (fail[0]) {
        error("failed!");
      }
    }
  }

}
