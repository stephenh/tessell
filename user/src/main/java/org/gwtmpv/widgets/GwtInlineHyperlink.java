package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;

public class GwtInlineHyperlink extends InlineHyperlink implements IsInlineHyperlink {

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
