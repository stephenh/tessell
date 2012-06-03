package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsFormPanel.class, stub = StubFormPanel.class)
public class FormPanel extends com.google.gwt.user.client.ui.FormPanel implements IsFormPanel {

  @Override
  public IsWidget getIsWidget() {
    return (IsWidget) getWidget();
  }

  @Override
  public void setWidget(IsWidget isWidget) {
    setWidget(isWidget.asWidget());
  }

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

}
