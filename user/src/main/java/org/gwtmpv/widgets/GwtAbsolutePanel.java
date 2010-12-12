package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class GwtAbsolutePanel extends AbsolutePanel implements IsAbsolutePanel {

  @Override
  public void add(IsWidget isWidget) {
    add(isWidget.asWidget());
  }

  @Override
  public boolean remove(IsWidget isWidget) {
    return remove(isWidget.asWidget());
  }

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
    return new GwtIsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

  @Override
  public void add(IsWidget w, int left, int top) {
    add(w.asWidget(), left, top);
  }

  @Override
  public int getIsWidgetLeft(IsWidget w) {
    return getWidgetLeft(w.asWidget());
  }

  @Override
  public int getIsWidgetTop(IsWidget w) {
    return getWidgetTop(w.asWidget());
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(w.asWidget(), beforeIndex);
  }

  @Override
  public void insert(IsWidget w, int left, int top, int beforeIndex) {
    insert(w.asWidget(), left, top, beforeIndex);
  }

  @Override
  public void setWidgetPosition(IsWidget w, int left, int top) {
    setWidgetPosition(w.asWidget(), left, top);
  }

}
