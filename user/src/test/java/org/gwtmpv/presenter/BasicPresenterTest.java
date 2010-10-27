package org.gwtmpv.presenter;

import static org.gwtmpv.widgets.Widgets.newFlowPanel;

import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.StubWidgetsProvider;
import org.junit.BeforeClass;
import org.junit.Test;

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
