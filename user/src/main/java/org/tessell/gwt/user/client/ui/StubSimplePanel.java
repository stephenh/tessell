package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

public class StubSimplePanel extends StubPanel implements IsSimplePanel {

  private IsWidget isWidget;

  @Override
  public void setWidget(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    this.isWidget = (IsWidget) isWidget;
  }

  @Override
  public void add(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    if (this.isWidget != null) {
      throw new IllegalArgumentException("SimplePanel already populated");
    }
    this.isWidget = (IsWidget) isWidget;
  }

  @Override
  public boolean remove(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    this.isWidget = null;
    return true;
  }

  @Override
  public void clear() {
    isWidget = null;
  }

  public IsWidget getDisplay() {
    return isWidget;
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    ArrayList<IsWidget> list = new ArrayList<IsWidget>();
    if (isWidget != null) {
      list.add(isWidget);
    }
    return list.iterator();
  }

  @Override
  public IsWidget getIsWidget() {
    return isWidget;
  }

}
