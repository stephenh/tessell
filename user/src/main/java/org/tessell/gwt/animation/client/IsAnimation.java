package org.tessell.gwt.animation.client;

import org.tessell.gwt.user.client.GwtTimer;
import org.tessell.gwt.user.client.StubTimer;

/** Abstracts {@link GwtTimer} vs. {@link StubTimer} for tests. */
public interface IsAnimation {

  void run(int duration);

  void cancel();

  /** Method only applicable to stubs to tell them not to auto-finish this animation. */
  void doNotAutoFinish();

  void setNewLogic(AnimationLogic logic);

}
