package org.tessell.tests.presenter;

import org.junit.Test;
import org.tessell.presenter.BasicPresenter;
import org.tessell.presenter.Presenter;
import org.tessell.widgets.IsWidget;

public class PresenterTest {

  @Test(expected = IllegalStateException.class)
  public void superOnBindMustBeCalled() {
    final NoSuperBindPresenter p = new NoSuperBindPresenter();
    p.bind();
  }

  @Test(expected = IllegalStateException.class)
  public void superOnUnbindMustBeCalled() {
    final NoSuperUnbindPresenter p = new NoSuperUnbindPresenter();
    p.bind();
    p.unbind();
  }

  @Test
  public void superBindAndUnbindCalled() {
    final BothSuperBindAndUnbindPresenter p = new BothSuperBindAndUnbindPresenter();
    p.bind();
    p.unbind();
  }

  @Test(expected = IllegalStateException.class)
  public void noRebind() {
    final Presenter p = new BasicPresenter<IsWidget>(null) {
    };
    p.bind();
    p.unbind();
    p.bind();
  }

  public class NoSuperBindPresenter extends BasicPresenter<IsWidget> {
    public NoSuperBindPresenter() {
      super(null);
    }

    @Override
    public void onBind() {
      // no super call
    }
  }

  public class NoSuperUnbindPresenter extends BasicPresenter<IsWidget> {
    public NoSuperUnbindPresenter() {
      super(null);
    }

    @Override
    public void onUnbind() {
      // no super call
    }
  }

  public class BothSuperBindAndUnbindPresenter extends BasicPresenter<IsWidget> {
    public BothSuperBindAndUnbindPresenter() {
      super(null);
    }

    @Override
    public void onBind() {
      super.onBind();
    }

    @Override
    public void onUnbind() {
      super.onUnbind();
    }
  }

}
