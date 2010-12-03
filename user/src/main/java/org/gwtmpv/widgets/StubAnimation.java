package org.gwtmpv.widgets;

/**
 * Implements {@link IsAnimation} for unit tests.
 *
 * By default, animations auto-finish. They assume most unit tests are
 * not interested in their animation functionality and so immediately
 * run at progress=0 and progress=1.
 *
 * For unit tests that <i>do</i> want to test the animation logic,
 * they should call {@link #doNotAutoFinish()} and then explicitly
 * {@link #tick(double)} the animation through its range of progress.
 */
public class StubAnimation implements IsAnimation {

  private AnimationLogic logic;
  private boolean autoFinish = true;
  private double currentProgress = -1;

  public StubAnimation(AnimationLogic logic) {
    this.logic = logic;
  }

  /** For tests to manually tick the animation through its progress. */
  public void tick(final double progress) {
    if (currentProgress == -1.0 || currentProgress == 1.0) {
      throw new IllegalStateException("Animation is not currently in progress");
    }
    if (progress == 0.0) {
      logic.onStart();
    } else if (progress == 1.0) {
      logic.onComplete();
    } else {
      logic.onUpdate(logic.interpolate(progress));
    }
    currentProgress = progress;
  }

  /**
   * Mark this instance to not auto-finish and instead wait for a unit
   * test to explicitly {@link #tick(double)} this to completion.
   */
  @Override
  public void doNotAutoFinish() {
    autoFinish = false;
  }

  @Override
  public void run(final int duration) {
    currentProgress = 0;
    if (autoFinish) {
      tick(0);
      tick(1);
    }
  }

  @Override
  public void cancel() {
    currentProgress = -1;
  }

  @Override
  public void setNewLogic(AnimationLogic logic) {
    this.logic = logic;
  }

  public boolean isRunning() {
    return currentProgress > -1;
  }

}
