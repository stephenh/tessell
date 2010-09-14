package org.gwtmpv.place;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.place.events.PlaceChangedEvent;
import org.gwtmpv.presenter.Presenter;
import org.gwtmpv.presenter.events.PresenterChangedEvent;
import org.gwtmpv.presenter.events.PresenterChangedEvent.PresenterChangedHandler;
import org.gwtmpv.presenter.events.PresenterRevealedEvent;
import org.gwtmpv.presenter.events.PresenterRevealedEvent.PresenterRevealedHandler;
import org.gwtmpv.presenter.events.PresenterUnboundEvent;
import org.gwtmpv.presenter.events.PresenterUnboundEvent.PresenterUnboundHandler;

/** This is a subclass of {@link Place} with some helper values for working with {@link Presenter}s. */
public abstract class PresenterPlace<T extends Presenter> extends Place {

  protected T currentPresenter;
  protected EventBus eventBus;

  public PresenterPlace(final EventBus eventBus, final String name) {
    super(name);
    this.eventBus = eventBus;
  }

  /** Put any state from <code>presenter</code> into the <code>request</code> for serialization as a token. */
  protected PlaceRequest prepareRequest(final PlaceRequest request, final T presenter) {
    return request;
  }

  /** Starts listening for {@link PresenterRevealedEvent}s and {@link PresenterChangedEvent}s. */
  @Override
  protected void onBind() {
    super.onBind();
    registerHandler(eventBus.addHandler(PresenterChangedEvent.getType(), new OnPresenterChanged()));
    registerHandler(eventBus.addHandler(PresenterRevealedEvent.getType(), new OnPresenterRevealed()));
    registerHandler(eventBus.addHandler(PresenterUnboundEvent.getType(), new OnPresenterUnbound()));
  }

  /** Translates {@link PresenterRevealedEvent}s to {@link PlaceChangedEvent}s. */
  private class OnPresenterRevealed implements PresenterRevealedHandler {
    public void onPresenterRevealed(final PresenterRevealedEvent event) {
      if (event.getPresenter() == currentPresenter) {
        // Presenter revealed will be going away...
        // final PlaceRequest request = prepareRequest(new PlaceRequest(getName()), currentPresenter);
        // firePlaceRevealed(request);
      }
    }
  }

  /** Translates {@link PresenterChangedEvent}s to {@link PlaceChangedEvent}s. */
  private final class OnPresenterChanged implements PresenterChangedHandler {
    public void onPresenterChanged(final PresenterChangedEvent event) {
      if (event.getPresenter() == currentPresenter) {
        final PlaceRequest request = prepareRequest(new PlaceRequest(getName()), currentPresenter);
        firePlaceChanged(request);
      }
    }
  }

  private final class OnPresenterUnbound implements PresenterUnboundHandler {
    public void onPresenterUnbound(final PresenterUnboundEvent event) {
      if (event.getPresenter() == currentPresenter) {
        currentPresenter = null;
      }
    }
  }

  /** Subclasses should call when they are changed and the PlaceManager should update the history token. */
  protected void firePlaceChanged(final PlaceRequest request) {
    eventBus.fireEvent(new PlaceChangedEvent(this, request));
  }
}
