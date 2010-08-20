package org.gwtmpv.widgets;

import com.google.gwt.animation.client.Animation;

public class GwtAnimations implements IsAnimations {

  @Override
  public IsAnimation wrap(final IsAnimationLogic logic) {
    final Animation real = new Animation() {
      @Override
      protected void onUpdate(final double progress) {
        logic.onUpdate(progress);
      }
    };
    return new IsAnimation() {
      @Override
      public void run(final int delay) {
        real.run(delay);
      }

      @Override
      public void cancel() {
        real.cancel();
      }
    };
  }

}
