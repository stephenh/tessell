package org.tessell.gwt.animation.client;

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

  public static void doAnimation() {
    StubAnimation a = currentAnimation();
    a.tick(0);
    a.tick(1);
  }

  public static void tickAnimation(double progress) {
    currentAnimation().tick(progress);
  }

  public static void finishAnimation() {
    currentAnimation().tick(1);
  }

  public static StubAnimation currentAnimation() {
    return captured.get(captured.size() - 1);
  }

  public static void clearCapture() {
    captured.clear();
    capture = false;
  }

}
