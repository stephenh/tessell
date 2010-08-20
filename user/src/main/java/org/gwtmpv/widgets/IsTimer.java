package org.gwtmpv.widgets;

public interface IsTimer {

  void cancel();

  void schedule(int delayMillis);

  void scheduleRepeating(int periodMillis);

}
