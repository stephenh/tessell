package org.gwtmpv.widgets;

/**
 * A base class for animation logic that is not tied to a
 * specific animation-running method (e.g. {@link GwtAnimation}
 * vs. {@link StubAnimation}).
 */
public abstract class AnimationLogic {

  public abstract void onUpdate(double progress);

  public void onComplete() {
  }

}
