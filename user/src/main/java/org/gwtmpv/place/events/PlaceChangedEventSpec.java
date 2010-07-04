package org.gwtmpv.place.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.place.Place;
import org.gwtmpv.place.PlaceRequest;

@GenEvent
public class PlaceChangedEventSpec {
  Place p1place;
  PlaceRequest p2request;
}
