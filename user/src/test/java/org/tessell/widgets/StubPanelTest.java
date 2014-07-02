package org.tessell.widgets;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubFlowPanel;
import org.tessell.gwt.user.client.ui.StubTextBox;

public class StubPanelTest {

  @Test
  public void testClear() {
    StubFlowPanel p = new StubFlowPanel();
    p.add(new StubTextBox());
    p.clear();
  }

}
