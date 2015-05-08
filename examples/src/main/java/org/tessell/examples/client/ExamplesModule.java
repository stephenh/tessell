package org.tessell.examples.client;

import org.tessell.examples.client.app.CheckboxExamplePresenter;
import org.tessell.examples.client.resources.AppResources;
import org.tessell.examples.client.views.AppViews;
import org.tessell.examples.client.views.GwtViewsProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class ExamplesModule implements EntryPoint {

  @Override
  public void onModuleLoad() {
    AppResources r = GWT.create(AppResources.class);
    r.fontAwesomeMin().ensureInjected();
    AppViews.setProvider(new GwtViewsProvider(r.fontAwesomeMin()));
    CheckboxExamplePresenter p = new CheckboxExamplePresenter();
    RootPanel.get().add(p.getView());
  }

}
