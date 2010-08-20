package org.gwtmpv.widgets;

public class StubAnimation implements IsAnimation {

  private final AnimationLogic logic;
  private boolean autoFinish = true;

  public StubAnimation(AnimationLogic logic) {
    this.logic = logic;
  }

  /** For tests to manually tick the animation through its progress. */
  public void tick(final double progress) {
    logic.onUpdate(progress);
    if (progress == 1.0) {
    }
  }

  public void doNotAutoFinish() {
    autoFinish = false;
  }

  @Override
  public void run(final int delay) {
    logic.onUpdate(0);
    if (autoFinish) {
      logic.onUpdate(1);
    }
  }

  @Override
  public void cancel() {
  }
}
