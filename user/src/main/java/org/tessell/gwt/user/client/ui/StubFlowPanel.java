package org.tessell.gwt.user.client.ui;

public class StubFlowPanel extends StubComplexPanel implements IsFlowPanel {

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget widget, int beforeIndex) {
    widgets.add(beforeIndex, (IsWidget) widget);
  }

}
