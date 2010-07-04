package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class GwtImage extends Image implements IsImage {

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
