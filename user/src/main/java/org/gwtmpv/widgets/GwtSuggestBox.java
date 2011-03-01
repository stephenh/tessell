package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class GwtSuggestBox extends SuggestBox implements IsSuggestBox {

  public GwtSuggestBox() {
    super();
  }

  public GwtSuggestBox(SuggestOracle oracle) {
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
