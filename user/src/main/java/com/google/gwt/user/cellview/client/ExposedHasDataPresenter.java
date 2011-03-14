package com.google.gwt.user.cellview.client;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

/** The parent class is package private to GWT, but I want to use it from the {@code org.gwtmpv.widgets} package. */
public class ExposedHasDataPresenter<T> extends HasDataPresenter<T> {

  public ExposedHasDataPresenter(HasData<T> display, int pageSize, ProvidesKey<T> keyProvider) {
    super(display, new StubView<T>(), pageSize, keyProvider);
  }

  /** The View is for DOM/HTML munging, which our stubs don't do, so just pass in a no-op version. */
  static class StubView<T> implements HasDataPresenter.View<T> {
    @Override
    public <H extends EventHandler> HandlerRegistration addHandler(H handler, Type<H> type) {
      return null;
    }

    @Override
    public void render(SafeHtmlBuilder sb, List<T> values, int start, SelectionModel<? super T> selectionModel) {
    }

    @Override
    public void replaceAllChildren(List<T> values, SafeHtml html, boolean stealFocus) {
    }

    @Override
    public void replaceChildren(List<T> values, int start, SafeHtml html, boolean stealFocus) {
    }

    @Override
    public void resetFocus() {
    }

    @Override
    public void setKeyboardSelected(int index, boolean selected, boolean stealFocus) {
    }

    @Override
    public void setLoadingState(LoadingState state) {
    }
  }

}
