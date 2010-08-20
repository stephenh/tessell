package org.gwtmpv.widgets;

/** Abstracts {@link GwtTimer} vs. {@link StubTimer} for tests. */
public interface IsAnimation {

  void run(int delay);

  void cancel();

}
