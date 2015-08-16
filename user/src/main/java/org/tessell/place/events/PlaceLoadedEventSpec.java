package org.tessell.place.events;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.place.Place;

@GenEvent
public class PlaceLoadedEventSpec {
  @Param(1)
  Place place;
}
