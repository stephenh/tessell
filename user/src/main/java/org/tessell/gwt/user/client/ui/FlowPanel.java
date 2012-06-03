package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsFlowPanel.class, stub = StubFlowPanel.class)
public class FlowPanel extends com.google.gwt.user.client.ui.FlowPanel implements IsFlowPanel {

  @Override
  public void add(final IsWidget isWidget) {
    add(isWidget.asWidget());
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
    return remove(isWidget.asWidget());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public void insert(IsWidget widget, int beforeIndex) {
    insert(widget.asWidget(), beforeIndex);
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

}
