package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.StubSimpleRadioButton;

public class StubRadioButton extends RadioButton {

  public StubRadioButton() {
    super("name"); // tessell can't pull this out of the ui.xml yet
  }

  public void click() {
    ((StubSimpleRadioButton) button).click();
  }

  public void check() {
    ((StubSimpleRadioButton) button).check();
  }

}
