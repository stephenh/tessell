package org.tessell.model.properties;

import java.util.ArrayList;
import java.util.List;

/** Abstraction for tracking dependent properties during evaluation. */
public class Upstream {

  private static List<Property<?>> implicitUpstream = null;

  /** Logs {@code p} as upstream of the current, if any, {@link Capture} */
  public static void addIfTracking(Property<?> p) {
    if (implicitUpstream != null && !implicitUpstream.contains(p)) {
      // Some other derived property is having it's get() called, so it depends on us now
      implicitUpstream.add(p);
    }
  }

  /** Starts a new capture. */
  public static Capture start() {
    return new Capture();
  }

  public static class Capture {
    // Turn on implicitUpstream, which watches for properties called during value.get.
    // Also, keep track if anyone was already tracking derived values so we can put it back.
    private final List<Property<?>> tempUpstream = Upstream.implicitUpstream;

    private Capture() {
      Upstream.implicitUpstream = new ArrayList<Property<?>>();
    }

    public List<Property<?>> finish() {
      List<Property<?>> newUpstream = new ArrayList<Property<?>>(implicitUpstream);
      // Put back the previous upstream before we do anything else
      Upstream.implicitUpstream = tempUpstream;
      return newUpstream;
    }

  }
}
