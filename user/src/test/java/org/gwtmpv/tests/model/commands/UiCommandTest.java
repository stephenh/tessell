package org.gwtmpv.tests.model.commands;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

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
  public void errorsToAnotherSource() {
    DummyHandlers h = new DummyHandlers();
    DummyUiCommand command = new DummyUiCommand();
    command.error(h, "Foo");
    assertThat(h.messages.get(0), is("Foo"));

    command.execute();
    assertThat(h.messages.size(), is(0));
  }

  @Test
  public void errorsAreClearedBeforeOnlyIfCheck() {
    final DummyHandlers h = new DummyHandlers();
    final DummyUiCommand command = new DummyUiCommand();
    final BooleanProperty noErrors = booleanProperty(new DerivedValue<Boolean>("noErrors") {
      public Boolean get() {
        return h.messages.size() == 0;
      }
    });
    command.addOnlyIf(noErrors);
    command.error(h, "Foo");
    assertThat(h.messages.get(0), is("Foo"));

    command.execute();
    assertThat(h.messages.size(), is(0));
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

  private final class DummyHandlers implements HasHandlers {
    final List<String> messages = new ArrayList<String>();

    public void fireEvent(GwtEvent<?> event) {
      if (event instanceof RuleTriggeredEvent) {
        messages.add(((RuleTriggeredEvent) event).getMessage());
      } else if (event instanceof RuleUntriggeredEvent) {
        messages.remove(((RuleUntriggeredEvent) event).getMessage());
      }
    }
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
