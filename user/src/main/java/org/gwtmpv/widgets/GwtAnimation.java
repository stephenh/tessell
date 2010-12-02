package org.gwtmpv.widgets;

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
    logic.onUpdate(progress);
  }

  @Override
  protected void onComplete() {
    logic.onComplete();
  }

  @Override
  public void doNotAutoFinish() {
    // noop
  }

  @Override
  public IsAnimation newLogic(AnimationLogic logic) {
    this.logic = logic;
    return this;
  }

}
