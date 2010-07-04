package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class GwtCheckBox extends CheckBox implements IsCheckBox {

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
