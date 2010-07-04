package org.gwtmpv.util;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.place.PlaceRequest;
import org.gwtmpv.place.events.PlaceRequestEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class PlaceOnClick {

  public static HandlerRegistration register(final EventBus eventBus, final HasClickHandlers foo,
                                             final PlaceRequest request) {
    return foo.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        eventBus.fireEvent(new PlaceRequestEvent(request));
      }
    });
  }

}
