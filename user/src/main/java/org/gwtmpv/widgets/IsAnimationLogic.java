package org.gwtmpv.widgets;

public abstract class IsAnimationLogic {

  public abstract void onUpdate(double progress);

  // copy/paste from Animation
  protected double interpolate(final double progress) {
    return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
  }

}
