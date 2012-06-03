package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsScrollPanel.class, stub = StubScrollPanel.class)
public class ScrollPanel extends com.google.gwt.user.client.ui.ScrollPanel implements IsScrollPanel {

  @Override
  public void setWidget(final IsWidget isWidget) {
    setWidget(isWidget.asWidget());
  }

  @Override
  public void add(final IsWidget isWidget) {
    add(isWidget.asWidget());
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
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
  public IsWidget getIsWidget() {
    return (IsWidget) getWidget();
  }

}
