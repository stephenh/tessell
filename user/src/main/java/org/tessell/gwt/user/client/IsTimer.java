package org.tessell.gwt.user.client;

public interface IsTimer {

  void cancel();

  void schedule(int delayMillis);

  void scheduleRepeating(int periodMillis);

  /** Method only applicable to stubs to tell them not to auto-run this timer. */
  void doNotAutoRun();

}
