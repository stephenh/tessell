package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.*;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubFocusWidget extends StubWidget implements IsFocusWidget {

  private static StubFocusWidget lastFocus;
  private boolean enabled = true;
  private boolean focus;
  private int tabIndex;
  private char accessKey;

  public void click() {
    // is only-click if enabled true for all FocusWidgets?
    if (!enabled) {
      throw new IllegalStateException(this + " is disabled");
    }
    fireEvent(new StubClickEvent());
  }

  /** @return only a vague heuristic of whether we're focused */
  public boolean isFocused() {
    return focus;
  }

  @Override
  public void setFocus(final boolean focused) {
    if (focused) {
      if (lastFocus != null) {
        lastFocus.setFocus(false);
      }
      focus = true;
      fireEvent(new StubFocusEvent());
      lastFocus = this;
    } else {
      focus = false;
      blur();
    }
  }

  public void focus() {
    fireEvent(new StubFocusEvent());
  }

  public void blur() {
    fireEvent(new StubBlurEvent());
  }

  public void mouseOver() {
    fireEvent(new StubMouseOverEvent());
  }

  public void mouseOut() {
    fireEvent(new StubMouseOutEvent());
  }

  public void keyUp(int keyCode) {
    keyUp((char) keyCode, new StubKeyModifiers());
  }

  public void keyUp(char keyCode) {
    keyUp(keyCode, new StubKeyModifiers());
  }

  public void keyUp(char keyCode, StubKeyModifiers mods) {
    fireEvent(new StubKeyUpEvent(keyCode, mods));
  }

  public void keyDown(int keyCode) {
    keyDown((char) keyCode, new StubKeyModifiers());
  }

  public void keyDown(char keyCode) {
    keyDown(keyCode, new StubKeyModifiers());
  }

  public void keyDown(char keyCode, StubKeyModifiers mods) {
    fireEvent(new StubKeyDownEvent(keyCode, mods));
  }

  public void keyPress(int c) {
    keyPress((char) c, new StubKeyModifiers());
  }

  public void keyPress(char c) {
    keyPress(c, new StubKeyModifiers());
  }

  public void keyPress(char c, StubKeyModifiers mods) {
    fireEvent(new StubKeyPressEvent(c, mods));
  }

  public void press(int c) {
    press((char) c);
  }

  public void press(char c) {
    keyDown(c);
    keyPress(c);
    keyUp(c);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
  }

  @Override
  public int getTabIndex() {
    return tabIndex;
  }

  public char getAccessKey() {
    return accessKey;
  }

  @Override
  public void setAccessKey(final char key) {
    accessKey = key;
  }

  @Override
  public void setTabIndex(final int index) {
    tabIndex = index;
  }

  @Override
  public HandlerRegistration addFocusHandler(final FocusHandler handler) {
    return handlers.addHandler(FocusEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addBlurHandler(final BlurHandler handler) {
    return handlers.addHandler(BlurEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler) {
    return handlers.addHandler(KeyUpEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler) {
    return handlers.addHandler(KeyDownEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyPressHandler(final KeyPressHandler handler) {
    return handlers.addHandler(KeyPressEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
    return handlers.addHandler(MouseDownEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
    return handlers.addHandler(MouseUpEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
    return handlers.addHandler(MouseOutEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
    return handlers.addHandler(MouseOverEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
    return handlers.addHandler(MouseMoveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
    return handlers.addHandler(MouseWheelEvent.getType(), handler);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

}
