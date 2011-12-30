package org.tessell.place.events;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.place.Place;
import org.tessell.place.PlaceRequest;

@GenEvent
public class PlaceChangedEventSpec {
  @Param(1)
  Place place;
  @Param(2)
  PlaceRequest request;
}
