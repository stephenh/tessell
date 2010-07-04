package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class GwtButton extends Button implements IsButton {

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

}
