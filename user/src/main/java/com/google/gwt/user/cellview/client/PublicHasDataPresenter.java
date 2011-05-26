package com.google.gwt.user.cellview.client;

import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;

/** A dummy class to make HasDataPresenter's functionality available outside its package. */
public class PublicHasDataPresenter<T> extends HasDataPresenter<T> {

  public PublicHasDataPresenter(HasData<T> display, PublicView<T> view, int pageSize, ProvidesKey<T> keyProvider) {
    super(display, view, pageSize, keyProvider);
  }

  /** A dummy class to make HasDataPresenter.View available outside its package. */
  public static abstract class PublicView<T> implements HasDataPresenter.View<T> {
    @Override
    public void setLoadingState(LoadingState state) {
      // LoadingState is package private, so provide a no-op here
    }
  }

}
