package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

public class AbsolutePanel extends com.google.gwt.user.client.ui.AbsolutePanel implements IsAbsolutePanel {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
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
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public int getIsWidgetLeft(com.google.gwt.user.client.ui.IsWidget w) {
    return getWidgetLeft(w.asWidget());
  }

  @Override
  public int getIsWidgetTop(com.google.gwt.user.client.ui.IsWidget w) {
    return getWidgetTop(w.asWidget());
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget w, int left, int top, int beforeIndex) {
    insert(w.asWidget(), left, top, beforeIndex);
  }

  @Override
  public void setWidgetPosition(com.google.gwt.user.client.ui.IsWidget w, int left, int top) {
    setWidgetPosition(w.asWidget(), left, top);
  }

}
