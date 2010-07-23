package org.gwtmpv.place.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.place.Place;
import org.gwtmpv.place.PlaceRequest;

@GenEvent
public class PlaceChangedEventSpec {
  @Param(1)
  Place place;
  @Param(2)
  PlaceRequest request;
}
