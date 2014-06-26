package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

public class TextArea extends com.google.gwt.user.client.ui.TextArea implements IsTextArea {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
  }

  @Override
  public com.google.gwt.user.client.ui.TextArea asWidget() {
    return this;
  }

  @Override
  public int getMaxLength() {
    String value = getElement().getAttribute("maxlength"); // only in HTML5
    return value == null ? 0 : Integer.parseInt(value);
  }

  @Override
  public void setMaxLength(int length) {
    getElement().setAttribute("maxlength", Integer.toString(length));
  }

}
