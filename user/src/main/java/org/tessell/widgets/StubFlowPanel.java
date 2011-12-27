package org.tessell.widgets;

public class StubFlowPanel extends StubComplexPanel implements IsFlowPanel {

  @Override
  public void insert(IsWidget widget, int beforeIndex) {
    widgets.add(beforeIndex, widget);
  }

}
