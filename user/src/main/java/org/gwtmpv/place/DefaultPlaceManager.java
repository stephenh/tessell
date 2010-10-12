package org.gwtmpv.place;

import java.util.HashMap;

import org.gwtmpv.place.events.PlaceChangedEvent;
import org.gwtmpv.place.events.PlaceChangedHandler;
import org.gwtmpv.place.events.PlaceRequestEvent;
import org.gwtmpv.place.events.PlaceRequestHandler;
import org.gwtmpv.place.history.IsHistory;
import org.gwtmpv.place.tokenizer.Tokenizer;
import org.gwtmpv.place.tokenizer.TokenizerException;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;

public class DefaultPlaceManager implements PlaceManager {

  private final EventBus eventBus;
  private final Tokenizer tokenizer;
  private final IsHistory history;
  private boolean alreadyHandling = false;
  protected final HashMap<String, Place> places = new HashMap<String, Place>();

  public DefaultPlaceManager(final EventBus eventBus, final Tokenizer tokenizer, final IsHistory history) {
    this.eventBus = eventBus;
    this.tokenizer = tokenizer;
    this.history = history;
    this.history.addValueChangeHandler(new OnHistoryValueChanged());
    eventBus.addHandler(PlaceRequestEvent.getType(), new OnPlaceRequest());
    eventBus.addHandler(PlaceChangedEvent.getType(), new OnPlaceChanged());
  }

  public void registerPlace(final Place place) {
    if (places.containsKey(place.getName())) {
      throw new IllegalStateException(place.getName() + " is already taken");
    }
    places.put(place.getName(), place);
    place.bind();
  }

  public void deregisterPlace(final Place place) {
    place.unbind();
    places.remove(place.getName());
  }

  /** Called when we can't parse the history token. */
  protected void handleInvalidToken(final String invalidToken) {
  }

  /** Updates History if it has changed, without firing another PlaceRequestEvent. */
  protected void setTokenWithoutEvent(final PlaceRequest request) {
    final String requestToken = tokenizer.toHistoryToken(request);
    final String currentToken = history.getToken();
    if (currentToken == null || !currentToken.equals(requestToken)) {
      try {
        // We call newItem(..., true) to notify other history listeners of the
        // change but but use alreadyHandling to note to ourselves that, when
        // this comes back via OnHistoryValueChanged, we are the originators
        alreadyHandling = true;
        history.newItem(requestToken, true);
      } finally {
        alreadyHandling = false;
      }
    }
  }

  /** Finds the place for {@code event} and calls {@link handleRequest}. */
  protected void handleRequest(final PlaceRequest request) {
    final Place place = places.get(request.getName());
    if (place != null) {
      setTokenWithoutEvent(request);
      place.handleRequest(request);
      eventBus.fireEvent(new PlaceChangedEvent(place, request));
    }
  }

  /** Fires a {@link PlaceRequestEvent} with the current history token, if present, otherwise return <code>false</code>. */
  public boolean fireCurrentPlace() {
    final String currentToken = history.getToken();
    if (currentToken != null && currentToken.trim().length() > 0) {
      history.fireCurrentHistoryState();
      return true;
    }
    return false;
  }

  private class OnPlaceChanged implements PlaceChangedHandler {
    public void onPlaceChanged(final PlaceChangedEvent event) {
      setTokenWithoutEvent(event.getRequest());
    }
  }

  /** Handles change events from {@link IsHistory}. */
  private class OnHistoryValueChanged implements ValueChangeHandler<String> {
    public void onValueChange(final ValueChangeEvent<String> event) {
      if (alreadyHandling) {
        return;
      }
      try {
        eventBus.fireEvent(new PlaceRequestEvent(tokenizer.toPlaceRequest(event.getValue())));
      } catch (final TokenizerException e) {
        handleInvalidToken(event.getValue());
      }
    }
  }

  /** Listens for {@link PlaceRequestEvent}s and calls {@link #handleRequest} if its for this instance. */
  private final class OnPlaceRequest implements PlaceRequestHandler {
    public void onPlaceRequest(final PlaceRequestEvent event) {
      handleRequest(event.getRequest());
    }
  }

  @Override
  public void fireCurrentOr(PlaceRequest defaultPlace) {
    if (!fireCurrentPlace()) {
      eventBus.fireEvent(defaultPlace.asEvent());
    }
  }

}
