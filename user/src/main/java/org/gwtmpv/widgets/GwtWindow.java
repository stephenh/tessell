package org.gwtmpv.widgets;

import com.google.gwt.user.client.Window;

public class GwtWindow implements IsWindow {

  @Override
  public void open(final String url, final String name, final String features) {
    Window.open(url, name, features);
  }

  @Override
  public void scrollTo(final int x, final int y) {
    Window.scrollTo(x, y);
  }

  @Override
  public int getClientHeight() {
    return Window.getClientHeight();
  }

  @Override
  public int getClientWidth() {
    return Window.getClientWidth();
  }

  @Override
  public int getScrollLeft() {
    return Window.getScrollLeft();
  }

  @Override
  public int getScrollTop() {
    return Window.getScrollTop();
  }

}
