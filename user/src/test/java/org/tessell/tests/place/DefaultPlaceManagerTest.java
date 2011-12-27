package org.tessell.tests.place;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.tessell.bus.StubEventBus;
import org.tessell.place.DefaultPlaceManager;
import org.tessell.place.Place;
import org.tessell.place.PlaceRequest;
import org.tessell.place.events.PlaceChangedEvent;
import org.tessell.place.history.StubHistory;
import org.tessell.place.tokenizer.DefaultTokenizer;

public class DefaultPlaceManagerTest {

  final StubEventBus bus = new StubEventBus();
  final DefaultTokenizer t = new DefaultTokenizer();
  final StubHistory h = new StubHistory();
  final DefaultPlaceManager manager = new DefaultPlaceManager(bus, t, h);

  @Test
  public void requestEventFollowsUpWithAChangeEvent() {
    DummyPlace p = new DummyPlace("someplace");
    manager.registerPlace(p);

    bus.fireEvent(new PlaceRequest("someplace").asEvent());

    assertThat(p.called, is(1));
    assertThat(bus.getEvent(PlaceChangedEvent.class, 0), not(nullValue()));
  }

  /** A place that just keeps track of the number of times its called. */
  private final class DummyPlace extends Place {
    int called;

    private DummyPlace(String name) {
      super(name);
    }

    @Override
    public void handleRequest(PlaceRequest request) {
      called++;
    }
  }

}
