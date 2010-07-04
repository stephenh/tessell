package org.gwtmpv.presenter.util;

import org.gwtmpv.presenter.Presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class RevealOnCallback<T> implements AsyncCallback<T> {

  private final Presenter p;

  public RevealOnCallback(final Presenter p) {
    this.p = p;
  }

  @Override
  public void onSuccess(final T result) {
    p.revealDisplay();
  }

  @Override
  public void onFailure(final Throwable caught) {
    // nothing--the UI/etc. should have errors pushed to it?
  }

}
