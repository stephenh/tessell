package org.tessell.widgets;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubButton;
import org.tessell.gwt.user.client.ui.StubFlowPanel;
import org.tessell.gwt.user.client.ui.StubTextBox;

public class StubViewTest {

  @Test
  public void testSetIsParent() {
    new SomeUiXmlView();
  }

  private static class SomeUiXmlView extends StubView {
    private final StubFlowPanel panel;
    private final StubTextBox box;
    private final SomeComponent component;

    private SomeUiXmlView() {
      panel = new StubFlowPanel();
      box = new StubTextBox();
      component = new SomeComponent();
      panel.add(box);
      panel.add(component);
      setWidget(box);
      ensureDebugId("SomeUiXml");
    }
  }

  private static class SomeComponent extends CompositeIsWidget {
    private final SomeComponentView view = new SomeComponentView();

    private SomeComponent() {
      setWidget(view);
      ensureDebugId("SomeComponent");
    }
  }

  private static class SomeComponentView extends StubView {
    private final StubButton b;

    private SomeComponentView() {
      b = new StubButton();
      setWidget(b);
      ensureDebugId("SomeComponent");
    }
  }

}
