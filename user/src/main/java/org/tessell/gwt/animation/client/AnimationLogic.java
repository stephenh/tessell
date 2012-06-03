package org.tessell.gwt.animation.client;


/**
 * A base class for animation logic that is not tied to a
 * specific animation-running method (e.g. {@link GwtAnimation}
 * vs. {@link StubAnimation}).
 *
 * This copy/pastes the {@code onStart}, {@code onComplete}, and
 * {@code interpolate} methods from GWT's Animation so that the
 * user can override them if needed. Both {@link GwtAnimation}
 * and {@link StubAnimation} will respect the user's {@code interpolate}
 * method.
 */
public abstract class AnimationLogic {

  /** Called with the already-interpolated {@code progress}. */
  public abstract void onUpdate(double progress);

  /** Called immediately before the animation starts. */
  public void onStart() {
    onUpdate(interpolate(0.0));
  }

  /** Called immediately after the animation completes. */
  public void onComplete() {
    onUpdate(interpolate(1.0));
  }

  public double interpolate(double progress) {
    return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
  }

}
