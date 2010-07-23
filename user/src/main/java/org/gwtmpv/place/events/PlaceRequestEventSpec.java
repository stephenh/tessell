package org.gwtmpv.place.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.place.PlaceRequest;

@GenEvent
public class PlaceRequestEventSpec {
  @Param(1)
  PlaceRequest request;
}
