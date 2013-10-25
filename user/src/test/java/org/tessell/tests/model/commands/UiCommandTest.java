package org.tessell.tests.model.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.tessell.model.properties.NewProperty.booleanProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.values.DerivedValue;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UiCommandTest extends AbstractRuleTest {

  @Test
  public void errorsAreClearedOnExecute() {
    DummyUiCommand command = new DummyUiCommand();
    listenTo(command);

    command.setFail(true);
    command.execute();
    assertMessages("failed!");

    command.setFail(false);
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
    assertThat(c.getExecutions(), is(0));
    assertThat(p1.isTouched(), is(true));
    assertThat(p2.isTouched(), is(true));

    p1.set(true);
    c.execute();
    assertThat(c.getExecutions(), is(0));

    p2.set(true);
    c.execute();
    assertThat(c.getExecutions(), is(1));
  }

  @Test
  public void executeFailsIfDisabled() {
    DummyUiCommand c = new DummyUiCommand();
    c.enabled().set(false);
    try {
      c.execute();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Command is disabled"));
    }
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

}
