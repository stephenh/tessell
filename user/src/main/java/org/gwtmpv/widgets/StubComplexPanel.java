package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StubComplexPanel extends StubPanel implements IsComplexPanel {

  protected final List<IsWidget> widgets = new ArrayList<IsWidget>();

  public List<IsWidget> get() {
    return widgets;
  }

  @Override
  public void add(final IsWidget isWidget) {
    widgets.add(isWidget);
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
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
  public int getWidgetIndex(IsWidget child) {
    return widgets.indexOf(child);
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return widgets.iterator();
  }

}
