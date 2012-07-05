package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

public class ScrollPanel extends com.google.gwt.user.client.ui.ScrollPanel implements IsScrollPanel {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget() {
    return (IsWidget) getWidget();
  }

}
