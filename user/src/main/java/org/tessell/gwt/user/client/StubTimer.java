package org.tessell.gwt.user.client;

/** Implements {@code IsTimer} for unit tests.
 * 
 * By default, stub timers run immediately without delay. The idea being that
 * most unit tests are not testing the timer and would * rather not have to
 * explicitly {@link #run()}.
 *
 * Tests that <i>do</i> want to test the timer should call {@link #doNotAutoRun()}
 * so that they can assert against the view state both pre-timer and post-timer.
 */
public class StubTimer implements IsTimer {

  private final Runnable runnable;
  private boolean isRepeating;
  private int delay;
  private int cancelCount = 0;
  private int scheduleCount = 0;
  // assumes most unit tests want this timer to just fire immediately
  private boolean autoRun = true;

  public StubTimer(Runnable runnable) {
    this.runnable = runnable;
  }

  // for tests
  public void run() {
    if (delay == -1) {
      throw new IllegalStateException("Not scheduled");
    }
    // mark us as cancelled before calling runnable.run in case it reschedules us
    if (!isRepeating) {
      delay = -1;
    }
    runnable.run();
  }

  /**
   * Mark this timer instance as not auto-run, so that tests can explicitly
   * invoke the timer logic via {@link #run()}.
   */
  @Override
  public void doNotAutoRun() {
    autoRun = false;
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
    if (autoRun) {
      run();
    }
  }

  @Override
  public void scheduleRepeating(final int periodMillis) {
    delay = periodMillis;
    isRepeating = true;
    if (autoRun) {
      run();
    }
  }

  public boolean isRepeating() {
    return isRepeating;
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public int getCancelCount() {
    return cancelCount;
  }

  public int getScheduleCount() {
    return scheduleCount;
  }

}
