package org.tessell.presenter;

import static org.tessell.widgets.Widgets.newFlowPanel;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tessell.gwt.user.client.ui.IsFlowPanel;
import org.tessell.presenter.BasicPresenter;
import org.tessell.widgets.StubWidgetsProvider;

public class BasicPresenterTest {

  @BeforeClass
  public static void installStubWidgets() {
    StubWidgetsProvider.install();
  }

  @Test(expected = IllegalStateException.class)
  public void getViewShouldFailIfUnbound() {
    TestPresenter p = new TestPresenter();
    p.getView();
  }

  private final class TestPresenter extends BasicPresenter<IsFlowPanel> {
    private TestPresenter() {
      super(newFlowPanel());
    }
  }
}
