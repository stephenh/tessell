package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StubComplexPanel extends StubPanel implements IsComplexPanel {

  protected final List<IsWidget> widgets = new ArrayList<IsWidget>();

  public List<IsWidget> get() {
    return widgets;
  }

  @Override
  public void add(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    widgets.add((IsWidget) isWidget);
  }

  @Override
  public boolean remove(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    return widgets.remove(isWidget);
  }

  @Override
  public void clear() {
    widgets.clear();
  }

  @Override
  public int getWidgetCount() {
    return widgets.size();
  }

  @Override
  public boolean remove(final int index) {
    return widgets.remove(index) != null;
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return widgets.get(index);
  }

  @Override
  public int getWidgetIndex(com.google.gwt.user.client.ui.IsWidget child) {
    return widgets.indexOf(child);
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return widgets.iterator();
  }

}
