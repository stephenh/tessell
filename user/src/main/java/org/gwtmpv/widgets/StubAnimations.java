package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

public class StubAnimations implements IsAnimations {

  private final List<IsAnimationLogic> outstanding = new ArrayList<IsAnimationLogic>();

  @Override
  public IsAnimation wrap(final IsAnimationLogic logic) {
    return new StubAnimation(logic);
  }

  public class StubAnimation implements IsAnimation {
    private final IsAnimationLogic logic;
    private boolean autoFinish = true;

    private StubAnimation(IsAnimationLogic logic) {
      this.logic = logic;
    }

    public void doNotAutoFinish() {
      autoFinish = false;
    }

    @Override
    public void run(final int delay) {
      logic.onUpdate(0);
      outstanding.add(logic);
      if (autoFinish) {
        logic.onUpdate(1);
      }
    }

    @Override
    public void cancel() {
      outstanding.remove(logic);
    }

    public void tick(final double progress) {
      logic.onUpdate(progress);
      if (progress == 1.0) {
        outstanding.remove(logic);
      }
    }
  }

}
