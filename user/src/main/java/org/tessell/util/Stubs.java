package org.tessell.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A way for stubs to reset themselves between tests.
 *
 * An app-specific AbstractPresenterTest should call Stubs.reset() in either
 * a before or after hook.
 */
public class Stubs {

  private static final List<Runnable> afterTestResets = new ArrayList<Runnable>();

  public static void addAfterTestReset(Runnable r) {
    afterTestResets.add(r);
  }

  public static void reset() {
    for (Runnable r : afterTestResets) {
      r.run();
    }
  }
}
