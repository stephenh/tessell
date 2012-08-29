package org.tessell.tests.model.commands;

import static org.tessell.model.properties.NewProperty.booleanProperty;

import org.tessell.model.commands.HasActive;
import org.tessell.model.properties.BooleanProperty;

/** Fails depending on the instance variable {@code fail}. */
public class DummyActiveCommand extends DummyUiCommand implements HasActive {

  private final BooleanProperty active = booleanProperty("active");
  private int outstanding = 0;

  public void done() {
    if (--outstanding == 0) {
      active.set(false);
    }
  }

  @Override
  public BooleanProperty active() {
    return active;
  }

  @Override
  protected void doExecute() {
    active.set(true);
    outstanding++;
    super.doExecute();
  }

}
