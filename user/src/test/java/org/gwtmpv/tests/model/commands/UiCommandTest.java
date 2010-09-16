package org.gwtmpv.tests.model.commands;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class UiCommandTest extends AbstractRuleTest {

  @Test
  public void errorsAreClearedOnExecute() {
    DummyUiCommand command = new DummyUiCommand();
    listenTo(command);

    command.fail = true;
    command.execute();
    assertMessages("failed!");

    command.fail = false;
    command.execute();
    assertMessages();
  }

  @Test
  public void executesOnlyIfAllOnlyIfsAreTrue() {
    BooleanProperty p1 = booleanProperty("p1", false);
    BooleanProperty p2 = booleanProperty("p2", false);

    DummyUiCommand c = new DummyUiCommand();
    c.addOnlyIf(p1);
    c.addOnlyIf(p2);

    c.execute();
    assertThat(c.executions, is(0));

    p1.set(true);
    c.execute();
    assertThat(c.executions, is(0));

    p2.set(true);
    c.execute();
    assertThat(c.executions, is(1));
  }

  /** Fails depending on the instance variable {@code fail}. */
  private final class DummyUiCommand extends UiCommand {
    private boolean fail = false;
    private int executions = 0;

    @Override
    protected void doExecute() {
      executions++;
      if (fail) {
        error("failed!");
      }
    }
  }

}
