package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.user.client.ui.SuggestOracle;

@OtherTypes(intf = IsSuggestBox.class, stub = StubSuggestBox.class)
public class SuggestBox extends com.google.gwt.user.client.ui.SuggestBox implements IsSuggestBox {

  public SuggestBox() {
    super();
  }

  public SuggestBox(SuggestOracle oracle) {
    super(oracle);
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
