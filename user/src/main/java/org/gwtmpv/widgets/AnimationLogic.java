package org.gwtmpv.widgets;

/**
 * An interface for animation logic that is not tied to a
 * specific animation-running method (e.g. {@link GwtAnimation}
 * vs. {@link StubAnimation}).
 */
public interface AnimationLogic {

  void onUpdate(double progress);

}
