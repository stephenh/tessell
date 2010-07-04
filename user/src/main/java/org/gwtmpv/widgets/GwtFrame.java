package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public class GwtFrame extends Frame implements IsFrame {

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
