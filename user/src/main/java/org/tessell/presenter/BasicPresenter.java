package org.tessell.presenter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.tessell.bus.AbstractBound;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.dsl.Binder;

/** A basic presenter that tracks bound handler registrations. */
public abstract class BasicPresenter<V extends IsWidget> extends AbstractBound implements Presenter {

  protected final Binder binder = new Binder();
  protected final V view;
  private ArrayList<Presenter> children;

  public BasicPresenter(final V view) {
    this.view = view;
  }

  /** @return The view for the presenter. */
  @Override
  public V getView() {
    // Previously we enforced that the presenter was bound, but this led to a lot
    // of false positives where:
    // 1. Presenter is bound, AJAX request fires in onBind()
    // 2. User clicks back, presenter is unbound
    // 3. AJAX response, fires onResult(), which tries to call getView()
    // 4. IllegalStateException on the presenter the user no longer cares about
    //
    // Perhaps ideally the AJAX command objects could be tied into the presenter
    // life cycle, and just immediately drop responses on the floor when on longer
    // bound. But it doesn't seem very harmful to let the now-disconnected onResult
    // update it's view, and then fairly soon get GC'd anyway. E.g. it should not
    // actively cause leaks.
    return view;
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
