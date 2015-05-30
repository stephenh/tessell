package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

public class PasswordTextBox extends com.google.gwt.user.client.ui.PasswordTextBox implements IsPasswordTextBox {

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
  }

  @Override
  public com.google.gwt.user.client.ui.PasswordTextBox asWidget() {
    return this;
  }

  @Override
  public void setPlaceholder(String placeholder) {
    getIsElement().setAttribute("placeholder", placeholder);
  }

  @Override
  public String getPlaceholder() {
    return getIsElement().getAttribute("placeholder");
  }
}
