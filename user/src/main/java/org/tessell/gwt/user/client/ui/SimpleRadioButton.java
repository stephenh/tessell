package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.uibinder.client.UiConstructor;

public class SimpleRadioButton extends com.google.gwt.user.client.ui.SimpleRadioButton implements IsSimpleRadioButton {

  @UiConstructor
  public SimpleRadioButton(String name) {
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
