package org.gwtmpv.presenter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.gwtmpv.bus.AbstractBound;
import org.gwtmpv.bus.EventBus;
import org.gwtmpv.presenter.events.PresenterChangedEvent;
import org.gwtmpv.presenter.events.PresenterRevealedEvent;
import org.gwtmpv.presenter.events.PresenterUnboundEvent;
import org.gwtmpv.presenter.events.PresenterRevealedEvent.PresenterRevealedHandler;
import org.gwtmpv.util.factory.Factory;
import org.gwtmpv.util.factory.Lazy;
import org.gwtmpv.widgets.IsWidget;

/** A basic presenter that tracks bound handler registrations. */
public abstract class BasicPresenter<V extends IsWidget> extends AbstractBound implements Presenter {

  protected final V view;
  protected final EventBus eventBus;
  private ArrayList<Presenter> children;

  public BasicPresenter(final V view, final EventBus eventBus) {
    this.view = view;
    this.eventBus = eventBus;
  }

  /** @return The view for the presenter. */
  public V getView() {
    return view;
  }

  /** Fires a {@link PresenterChangedEvent} to the {@link EventBus}. */
  protected void firePresenterChangedEvent() {
    eventBus.fireEvent(new PresenterChangedEvent(this));
  }

  /** Fires a {@link PresenterRevealedEvent} to the {@link EventBus}. */
  protected void firePresenterRevealedEvent() {
    eventBus.fireEvent(new PresenterRevealedEvent(this));
  }

  /** Triggers a {@link PresenterRevealedEvent} after calling {@link #onRevealDisplay()}. */
  public final void revealDisplay() {
    if (!isBound()) {
      throw new IllegalStateException("Presenter is not bound yet " + this);
    }
    onRevealDisplay();
    firePresenterRevealedEvent();
  }

  /*
   * The reveal/hide idiom is not flushed out very well--ideally it should be super easy for parent presenters to switch
   * from one tab/child to another, without it having to be a ContainerPresenter and forced to only have a single
   * "current" child. E.g. you should get slots or something like that.
   */
  public final void hideDisplay() {
    if (!isBound()) {
      throw new IllegalStateException("Presenter is not bound yet " + this);
    }
    onHideDisplay();
  }

  /** Method for subclasses to perform view logic. */
  protected void onRevealDisplay() {
  }

  /** Method for subclasses to perform view logic. */
  protected void onHideDisplay() {
  }

  /** Method for subclasses to perform view logic. */
  protected void onChildRevealed(final Presenter presenter) {
  }

  @Override
  protected void onBind() {
    super.onBind();

    // bind anybody that got added before we were bound
    if (children != null) {
      registerHandler(eventBus.addHandler(PresenterRevealedEvent.getType(), new OnPossibleChildRevelead()));
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
    eventBus.fireEvent(new PresenterUnboundEvent(this));
  }

  protected <C extends Presenter> C addPresenter(final C child) {
    if (children().add(child)) {
      if (isBound()) {
        child.bind();
      }
    }
    return child;
  }

  protected void removePresenter(final Presenter child) {
    if (children().remove(child)) {
      child.unbind();
    } else {
      throw new NoSuchElementException("Presenter was not a child of ours " + child);
    }
  }

  protected <C1 extends Presenter> Lazy<C1> lazyAdd(final Factory<C1> original) {
    final Factory<C1> addOnMake = new Factory<C1>() {
      public C1 create() {
        return addPresenter(original.create());
      }
    };
    return Lazy.of(addOnMake);
  }

  private ArrayList<Presenter> children() {
    if (children == null) {
      if (isBound()) {
        registerHandler(eventBus.addHandler(PresenterRevealedEvent.getType(), new OnPossibleChildRevelead()));
      }
      children = new ArrayList<Presenter>();
    }
    return children;
  }

  private class OnPossibleChildRevelead implements PresenterRevealedHandler {
    public void onPresenterRevealed(final PresenterRevealedEvent event) {
      if (children == null) {
        return;
      }
      final Presenter presenter = event.getPresenter();
      if (children.contains(presenter)) {
        onChildRevealed(presenter);
      }
    }
  }

}
