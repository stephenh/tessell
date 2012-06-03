package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsHorizontalPanel.class, stub = StubHorizontalPanel.class)
public class HorizontalPanel extends com.google.gwt.user.client.ui.HorizontalPanel implements IsHorizontalPanel {

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
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(0);
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(w.asWidget(), beforeIndex);
  }

}
