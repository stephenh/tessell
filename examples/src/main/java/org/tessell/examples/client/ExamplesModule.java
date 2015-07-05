package org.tessell.examples.client;

import org.tessell.examples.client.app.DragAndDropExamplePresenter;
import org.tessell.examples.client.views.AppViews;
import org.tessell.examples.client.views.GwtViewsProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class ExamplesModule implements EntryPoint {

  @Override
  public void onModuleLoad() {
    AppViews.setProvider(new GwtViewsProvider());
    DragAndDropExamplePresenter p = new DragAndDropExamplePresenter();
    p.bind();
    RootPanel.get().add(p.getView());
  }

}
