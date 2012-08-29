package org.tessell.presenter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.tessell.bus.AbstractBound;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.dsl.Binder;

import com.google.gwt.user.client.ui.Widget;

/** A basic presenter that tracks bound handler registrations. */
public abstract class BasicPresenter<V extends IsWidget> extends AbstractBound implements Presenter, com.google.gwt.user.client.ui.IsWidget {

  protected final Binder binder = new Binder();
  protected final V view;
  private ArrayList<Presenter> children;

  public BasicPresenter(final V view) {
    this.view = view;
  }

  /** @return The view for the presenter. */
  @Override
  public V getView() {
    if (!isBound()) {
      throw new IllegalStateException(this + " has not been bound");
    }
    return view;
  }

  /**
   * So we can pretend we're a widget to GWT APIs.
   *
   * I'm not entirely sure when this would be useful--perhaps for referencing
   * presenters in ui.xml files directly instead of needing panels.
   */
  @Override
  public Widget asWidget() {
    return getView().asWidget();
  }

  @Override
  protected void onBind() {
    super.onBind();
    // allow handler registrations
    binder.bind();
    // bind anybody that got added before we were bound
    if (children != null) {
      for (final Presenter child : children) {
        child.bind();
      }
    }
  }

  @Override
  protected void onUnbind() {
    super.onUnbind();
    if (children != null) {
      for (final Presenter child : children) {
        child.unbind();
      }
    }
    binder.unbind();
  }

  /** Adds {@code child} as a child presenter, and binds it if we're already bound. */
  public <C extends Presenter> C addPresenter(final C child) {
    if (children().add(child)) {
      if (isBound()) {
        child.bind();
      }
    }
    return child;
  }

  /** Removes {@code child} as a child presenter, and unbinds it. */
  public void removePresenter(final Presenter child) {
    if (children().remove(child)) {
      child.unbind();
    } else {
      throw new NoSuchElementException("Presenter was not a child of ours " + child);
    }
  }

  /** @return our list of child presenters, lazily instantiated. */
  protected ArrayList<Presenter> children() {
    if (children == null) {
      children = new ArrayList<Presenter>();
    }
    return children;
  }

}
