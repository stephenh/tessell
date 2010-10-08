package org.gwtmpv.util;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.place.events.PlaceChangedEvent;
import org.gwtmpv.place.events.PlaceChangedEvent.PlaceChangedHandler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;

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
