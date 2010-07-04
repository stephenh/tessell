package org.gwtmpv.widgets;

public interface IsWindow {

  void open(String url, String name, String features);

  void scrollTo(int x, int y);

  int getScrollTop();

  int getScrollLeft();

  int getClientHeight();

  int getClientWidth();

}
