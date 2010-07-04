package org.gwtmpv.widgets;

public class StubTimer implements IsTimer {

  private Runnable runnable;
  private boolean isRepeating;
  private int delay;
  private int cancelCount = 0;
  private int scheduleCount = 0;

  // for tests
  public void run() {
    if (delay == -1) {
      throw new IllegalStateException("Not scheduled");
    }
    runnable.run();
    if (!isRepeating) {
      delay = -1;
    }
  }

  @Override
  public void cancel() {
    cancelCount++;
    delay = -1;
    isRepeating = false;
  }

  @Override
  public void schedule(final int delayMillis) {
    scheduleCount++;
    delay = delayMillis;
    isRepeating = false;
  }

  @Override
  public void scheduleRepeating(final int periodMillis) {
    delay = periodMillis;
    isRepeating = true;
  }

  @Override
  public void setRunnable(final Runnable runnable) {
    this.runnable = runnable;
  }

  public boolean isRepeating() {
    return isRepeating;
  }

  public int getDelay() {
    return delay;
  }

  public int getCancelCount() {
    return cancelCount;
  }

  public int getScheduleCount() {
    return scheduleCount;
  }

}
