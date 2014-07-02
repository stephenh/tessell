package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tessell.widgets.CompositeIsWidget;
import org.tessell.widgets.StubWidget;

public class StubComplexPanel extends StubPanel implements IsComplexPanel {

  private final List<IsWidget> widgets = new ArrayList<IsWidget>();

  public List<IsWidget> get() {
    return widgets;
  }

  @Override
  public void add(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    widgets.add((IsWidget) isWidget);
    setIsParent(isWidget, this);
  }

  @Override
  public boolean remove(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    setIsParent(isWidget, null);
    return widgets.remove(isWidget);
  }

  @Override
  public void clear() {
    for (IsWidget w : new ArrayList<IsWidget>(widgets)) {
      remove(w);
    }
  }

  @Override
  public int getWidgetCount() {
    return widgets.size();
  }

  @Override
  public boolean remove(final int index) {
    return remove(widgets.get(index));
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

  protected void add(int index, final com.google.gwt.user.client.ui.IsWidget isWidget) {
    widgets.add(index, (IsWidget) isWidget);
    setIsParent(isWidget, this);
  }

  private static void setIsParent(com.google.gwt.user.client.ui.IsWidget isWidget, IsWidget parent) {
    // a CompositeIsWidget might itself use a CompositeIsWidget as its view
    while (isWidget instanceof CompositeIsWidget) {
      isWidget = ((CompositeIsWidget) isWidget).getIsWidget();
    }
    ((StubWidget) isWidget).setIsParent(parent);
  }

}
