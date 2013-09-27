package org.tessell.gwt.animation.client;

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
  // starts out -1, moved to 0-1 when in progress, or -2 when cancelled. Yes, janky.
  private double currentProgress = -1;
  private int requestedDuration = -1;

  public StubAnimation(AnimationLogic logic) {
    this.logic = logic;
  }

  /** For tests to manually tick the animation through its progress. */
  public void tick(final double progress) {
    if (progress < 0 || progress > 1.0) {
      throw new IllegalStateException("New progress must be between 0 and 1");
    }
    if (isCancelled()) {
      throw new IllegalStateException("Animation is cancelled");
    }
    if (!isRunning()) {
      throw new IllegalStateException("Animation is not currently in progress");
    }
    if (isFinished()) {
      throw new IllegalStateException("Animation is already finished");
    }
    if (progress == 0.0) {
      logic.onStart();
    }
    logic.onUpdate(logic.interpolate(progress));
    if (progress == 1.0) {
      logic.onComplete();
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
    requestedDuration = duration;
    currentProgress = 0;
    if (autoFinish) {
      tick(0);
      tick(1);
    }
  }

  public int getRequestedDuration() {
    return requestedDuration;
  }

  public void setRequestedDuration(int requestedDuration) {
    this.requestedDuration = requestedDuration;
  }

  @Override
  public void cancel() {
    currentProgress = -2;
  }

  @Override
  public void setNewLogic(AnimationLogic logic) {
    this.logic = logic;
  }

  public boolean isRunning() {
    return currentProgress > -1 && currentProgress < 1;
  }

  public boolean isCancelled() {
    return currentProgress == -2;
  }

  public boolean isFinished() {
    return currentProgress == 1;
  }

}
