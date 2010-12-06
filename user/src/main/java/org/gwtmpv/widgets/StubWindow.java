package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;

public class StubWindow implements IsWindow {

  private final EventBus handlers = new SimplerEventBus();
  public final List<String> alerts = new ArrayList<String>();
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

  @Override
  public int getScrollHeight() {
    return 0;
  }

  @Override
  public int getScrollWidth() {
    return 0;
  }

  @Override
  public HandlerRegistration addResizeHandler(final ResizeHandler handler) {
    return handlers.addHandler(ResizeEvent.getType(), handler);
  }

  @Override
  public void alert(String message) {
    alerts.add(message);
  }

}
