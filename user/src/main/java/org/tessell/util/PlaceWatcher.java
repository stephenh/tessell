package org.tessell.util;

import org.tessell.place.events.PlaceChangedEvent;
import org.tessell.place.events.PlaceChangedHandler;

import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/** Updates a {@code HasText} with the name of the place we're on, for testing. */
public class PlaceWatcher {

  private final HasText placeNameText;

  public PlaceWatcher(HasText placeNameText) {
    this.placeNameText = placeNameText;
  }

  public HandlerRegistration listenTo(EventBus eventBus) {
    return eventBus.addHandler(PlaceChangedEvent.getType(), new OnPlaceChanged());
  }

  /** Change place. */
  private class OnPlaceChanged implements PlaceChangedHandler {
    public void onPlaceChanged(PlaceChangedEvent event) {
      placeNameText.setText(event.getRequest().getName());
    }
  }
}
