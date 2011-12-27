package org.tessell.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FormPanel;

public class GwtFormPanel extends FormPanel implements IsFormPanel {

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
    return new GwtIsWidgetIteratorAdaptor(iterator());
  }

}
