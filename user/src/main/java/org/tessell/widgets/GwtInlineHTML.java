package org.tessell.widgets;

import com.google.gwt.user.client.ui.InlineHTML;

public class GwtInlineHTML extends InlineHTML implements IsInlineHTML {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
