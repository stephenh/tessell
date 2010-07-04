package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public class StubFlowPanel extends StubPanel implements IsFlowPanel {

  // Should be in StubComplexPanel
  private final List<IsWidget> displays = new ArrayList<IsWidget>();

  public List<IsWidget> get() {
    return displays;
  }

  public IsWidget getDisplay(final int index) {
    return displays.get(index);
  }

  @Override
  public void add(final IsWidget isWidget) {
    displays.add(isWidget);
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
    return displays.remove(isWidget);
  }

  @Override
  public void clear() {
    displays.clear();
  }

  @Override
  public void insert(final Widget w, final int beforeIndex) {
    throw new IllegalArgumentException("This is a stub.");
  }

  @Override
  public Widget getWidget(final int index) {
    throw new IllegalArgumentException("This is a stub.");
  }

  @Override
  public int getWidgetCount() {
    return displays.size();
  }

  @Override
  public int getWidgetIndex(final Widget child) {
    throw new IllegalArgumentException("This is a stub.");
  }

  @Override
  public boolean remove(final int index) {
    if (displays.size() > index) {
      displays.remove(index);
      return true;
    }
    return false;
  }

}