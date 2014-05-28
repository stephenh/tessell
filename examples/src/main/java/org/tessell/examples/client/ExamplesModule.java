package org.tessell.examples.client;

import org.tessell.examples.client.views.AppViews;
import org.tessell.examples.client.views.GwtViewsProvider;

import com.google.gwt.core.client.EntryPoint;

public class ExamplesModule implements EntryPoint {

  @Override
  public void onModuleLoad() {
    // AppResources r = GWT.create(AppResources.class);
    AppViews.setProvider(new GwtViewsProvider());
    // RootPanel.get().add(p.getView());
  }

}
