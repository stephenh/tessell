package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;

public class StubFlowPanel extends StubComplexPanel implements IsFlowPanel {

  @Override
  public void insert(IsWidget widget, int beforeIndex) {
    widgets.add(beforeIndex, widget);
  }

}
