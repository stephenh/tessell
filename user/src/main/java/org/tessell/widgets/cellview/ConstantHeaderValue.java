package org.tessell.widgets.cellview;

public class ConstantHeaderValue<C> extends HeaderValue<C> {

  private final C value;

  public ConstantHeaderValue(C value) {
    this.value = value;
  }

  @Override
  public C get() {
    return value;
  }

}
