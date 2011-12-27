package org.tessell.widgets;

import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubScrollPanel extends StubSimplePanel implements IsScrollPanel {

  @Override
  public int getHorizontalScrollPosition() {
    return 0;
  }

  @Override
  public int getScrollPosition() {
    return 0;
  }

  @Override
  public void scrollToBottom() {
  }

  @Override
  public void scrollToLeft() {

  }

  @Override
  public void scrollToRight() {
  }

  @Override
  public void scrollToTop() {
  }

  @Override
  public void setAlwaysShowScrollBars(final boolean alwaysShow) {
  }

  @Override
  public void setHorizontalScrollPosition(final int position) {
  }

  @Override
  public void setScrollPosition(final int position) {
  }

  @Override
  public HandlerRegistration addScrollHandler(final ScrollHandler handler) {
    return null;
  }

  @Override
  public void onResize() {
  }

  @Override
  public void setHeight(final String height) {
  }

  @Override
  public void setWidth(final String width) {
  }

}
