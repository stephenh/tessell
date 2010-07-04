package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PopupPanel;

public class StubPopupPanel extends StubSimplePanel implements IsPopupPanel {

  private boolean shown;
  public int popupLeft;
  public int popopTop;

  @Override
  public void center() {
    show();
  }

  @Override
  public void hide() {
    shown = false;
    CloseEvent.fire(this, null);
  }

  @Override
  public void show() {
    shown = true;
  }

  @Override
  public void setGlassEnabled(final boolean enabled) {
  }

  @Override
  public void setGlassStyleName(final String styleName) {
  }

  @Override
  public boolean isAnimationEnabled() {
    return false;
  }

  @Override
  public void setAnimationEnabled(final boolean enable) {
  }

  @Override
  public HandlerRegistration addCloseHandler(final CloseHandler<PopupPanel> handler) {
    return handlers.addHandler(CloseEvent.getType(), handler);
  }

  public boolean isShown() {
    return shown;
  }

  @Override
  public void setPopupPosition(final int left, final int top) {
    popupLeft = left;
    popopTop = top;
  }
}
