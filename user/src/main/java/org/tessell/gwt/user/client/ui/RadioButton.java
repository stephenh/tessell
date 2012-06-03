package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsRadioButton.class, stub = StubRadioButton.class)
public class RadioButton extends com.google.gwt.user.client.ui.RadioButton implements IsRadioButton {

  public RadioButton(String name) {
    super(name);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
