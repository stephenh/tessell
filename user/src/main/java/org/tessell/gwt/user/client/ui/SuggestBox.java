package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBoxBase;

public class SuggestBox extends com.google.gwt.user.client.ui.SuggestBox implements IsSuggestBox {

  public SuggestBox() {
    super();
  }

  public SuggestBox(SuggestOracle oracle) {
    super(oracle);
  }

  public SuggestBox(SuggestOracle oracle, TextBoxBase base) {
    super(oracle, base);
  }

  public SuggestBox(SuggestOracle oracle, TextBoxBase base, SuggestionDisplay display) {
    super(oracle, base, display);
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
