package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.Iterator;

public class StubSimplePanel extends StubPanel implements IsSimplePanel {

  private IsWidget isWidget;

  @Override
  public void setWidget(final IsWidget isWidget) {
    this.isWidget = isWidget;
  }

  @Override
  public void add(final IsWidget isWidget) {
    if (this.isWidget != null) {
      throw new IllegalArgumentException("SimplePanel already populated");
    }
    this.isWidget = isWidget;
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
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
    list.add(isWidget);
    return list.iterator();
  }

}
