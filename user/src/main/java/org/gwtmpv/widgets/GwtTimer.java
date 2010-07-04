package org.gwtmpv.widgets;

import com.google.gwt.user.client.Timer;

public class GwtTimer implements IsTimer {

  private final Timer timer = new Timer() {
    public void run() {
      if (runnable != null) {
        runnable.run();
      }
    }
  };
  private Runnable runnable;

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
  public void setRunnable(final Runnable runnable) {
    this.runnable = runnable;
  }

}
