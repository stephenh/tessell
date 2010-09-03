package org.gwtmpv.widgets;

/** Abstracts {@link GwtTimer} vs. {@link StubTimer} for tests. */
public interface IsAnimation {

  void run(int duration);

  void cancel();

  /** Method only applicable to stubs to tell them not to auto-finish this animation. */
  void doNotAutoFinish();

  IsAnimation newLogic(AnimationLogic logic);

}
