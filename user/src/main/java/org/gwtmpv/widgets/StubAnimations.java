package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

/** Watches stub animations go by and holds them for test poking. */
public class StubAnimations {

  private static final List<StubAnimation> captured = new ArrayList<StubAnimation>();
  private static boolean capture = false;

  public static void captureAnimations() {
    capture = true;
  }

  public static void captureIfNeeded(StubAnimation a) {
    if (capture) {
      a.doNotAutoFinish();
      captured.add(a);
    }
  }

  public static void doNextAnimation() {
    StubAnimation a = captured.remove(0);
    a.tick(0);
    a.tick(1);
  }

  public static void tickNextAnimation(double progress) {
    captured.get(0).tick(progress);
  }

  public static int getNextAnimationDuration() {
    return captured.get(0).getRequestedDuration();
  }

  public static void clearCapture() {
    captured.clear();
    capture = false;
  }

}
