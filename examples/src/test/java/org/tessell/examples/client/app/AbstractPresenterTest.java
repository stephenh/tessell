package org.tessell.examples.client.app;

import org.tessell.examples.client.views.AppViews;
import org.tessell.examples.client.views.StubViewsProvider;
import org.tessell.presenter.Presenter;

public abstract class AbstractPresenterTest {

  static {
    AppViews.setProvider(new StubViewsProvider());
  }

  protected static <P extends Presenter> P bind(P presenter) {
    presenter.bind();
    return presenter;
  }
}
