package org.tessell.gwt.animation.client;


import com.google.gwt.animation.client.Animation;

/** Implements {@link IsAnimation} for a generic {@code logic} by extending GWT's Animation class. */
public final class GwtAnimation extends Animation implements IsAnimation {

  private AnimationLogic logic;

  public GwtAnimation(final AnimationLogic logic) {
    this.logic = logic;
  }

  @Override
  public void run(final int duration) {
    super.run(duration);
  }

  @Override
  public void cancel() {
    super.cancel();
  }

  @Override
  protected void onUpdate(final double progress) {
    // progress is already interpolated by the Animation base class
    logic.onUpdate(progress);
  }

  @Override
  public void onStart() {
    logic.onStart();
  }

  @Override
  protected void onComplete() {
    logic.onComplete();
  }

  @Override
  protected double interpolate(double progress) {
    // defer to the user's interpolation method
    return logic.interpolate(progress);
  }

  @Override
  public void doNotAutoFinish() {
    // noop
  }

  @Override
  public void setNewLogic(AnimationLogic logic) {
    this.logic = logic;
  }

}
