package org.gwtmpv.widgets;

import static org.gwtmpv.widgets.Widgets.getRootPanel;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

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
    if (shown) {
      getRootPanel().remove(this);
      shown = false;
      CloseEvent.fire(this, null);
    }
  }

  @Override
  public void show() {
    if (!shown) {
      getRootPanel().add(this);
      shown = true;
    }
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

  @Override
  public boolean isShowing() {
    return shown;
  }

  @Override
  public void setPopupPosition(final int left, final int top) {
    popupLeft = left;
    popopTop = top;
  }

  @Override
  public void setPopupPositionAndShow(PositionCallback callback) {
    show();
    callback.setPosition(getOffsetWidth(), getOffsetHeight());
  }

  @Override
  public void addAutoHidePartner(IsElement element) {
  }

}
