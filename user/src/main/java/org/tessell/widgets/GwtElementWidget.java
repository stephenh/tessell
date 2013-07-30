package org.tessell.widgets;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;

public class GwtElementWidget extends FocusWidget implements IsElementWidget {

  public GwtElementWidget(Element element) {
    super(element);
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
