package org.tessell.place.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.tessell.place.PlaceRequest;

@GenEvent
public class PlaceRequestEventSpec {
  @Param(1)
  PlaceRequest request;
}
