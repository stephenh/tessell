package org.tessell.tests.place;

import org.tessell.place.Place;
import org.tessell.place.PlaceRequest;

/** A place that just keeps track of the number of times its called. */
public class DummyPlace extends Place {
  public int called;

  public DummyPlace(String name) {
    super(name);
  }

  @Override
  public void handleRequest(PlaceRequest request) {
    called++;
  }
}
