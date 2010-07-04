package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

public class StubAnimations implements IsAnimations {

  private final List<IsAnimationLogic> outstanding = new ArrayList<IsAnimationLogic>();

  @Override
  public IsAnimation wrap(final IsAnimationLogic logic) {
    return new IsAnimation() {
      @Override
      public void run(final int delay) {
        logic.onUpdate(0);
        outstanding.add(logic);
      }
    };
  }

  public void tick(final double progress) {
    for (final IsAnimationLogic logic : outstanding) {
      logic.onUpdate(progress);
    }
    if (progress == 1.0) {
      outstanding.clear();
    }
  }

}
