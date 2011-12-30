package org.tessell.place.events;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.place.PlaceRequest;

@GenEvent
public class PlaceRequestEventSpec {
  @Param(1)
  PlaceRequest request;
}
