package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsInlineHTML.class, stub = StubInlineHTML.class)
public class InlineHTML extends com.google.gwt.user.client.ui.InlineHTML implements IsInlineHTML {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
