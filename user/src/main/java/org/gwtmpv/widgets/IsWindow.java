package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface IsWindow {

  void reload();

  void alert(String message);

  void open(String url, String name, String features);

  void scrollTo(int x, int y);

  int getScrollTop();

  int getScrollLeft();

  int getScrollHeight();

  int getScrollWidth();

  int getClientHeight();

  int getClientWidth();

  HandlerRegistration addResizeHandler(ResizeHandler handler);

}
