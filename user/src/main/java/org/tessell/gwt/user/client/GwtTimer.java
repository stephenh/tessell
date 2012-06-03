package org.tessell.gwt.user.client;

import com.google.gwt.user.client.Timer;

/** Implements {@code IsTimer} using the real GWT timers. */
public class GwtTimer implements IsTimer {

  private final Runnable runnable;
  private final Timer timer = new Timer() {
    public void run() {
      runnable.run();
    }
  };

  public GwtTimer(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void cancel() {
    timer.cancel();
  }

  @Override
  public void schedule(final int delayMillis) {
    timer.schedule(delayMillis);
  }

  @Override
  public void scheduleRepeating(final int periodMillis) {
    timer.scheduleRepeating(periodMillis);
  }

  @Override
  public void doNotAutoRun() {
    // noop
  }

}
