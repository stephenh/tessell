package org.gwtmpv.widgets;

public class StubIsWindow implements IsWindow {

  public String open;
  public int x = -1;
  public int y = -1;

  @Override
  public void open(final String url, final String name, final String features) {
    open = url + "," + name + "," + features;
  }

  @Override
  public void scrollTo(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int getClientHeight() {
    return 0;
  }

  @Override
  public int getClientWidth() {
    return 0;
  }

  @Override
  public int getScrollLeft() {
    return x;
  }

  @Override
  public int getScrollTop() {
    return y;
  }

}
