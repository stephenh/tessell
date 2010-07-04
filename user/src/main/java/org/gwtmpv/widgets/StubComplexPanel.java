package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.Widget;

public class StubComplexPanel extends StubPanel implements IsComplexPanel {

  @Override
  public void add(final IsWidget isWidget) {
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
    return false;
  }

  @Override
  public void clear() {
  }

  @Override
  public Widget getWidget(final int index) {
    return null;
  }

  @Override
  public int getWidgetCount() {
    return 0;
  }

  @Override
  public int getWidgetIndex(final Widget child) {
    return 0;
  }

  @Override
  public boolean remove(final int index) {
    return false;
  }

}
