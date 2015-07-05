package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.*;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;

@SuppressWarnings("deprecation")
public class StubFocusWidget extends StubWidget implements IsFocusWidget {

  public StubFocusWidget() {
  }

  public StubFocusWidget(StubElement element) {
    super(element);
  }

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
    keyUp(keyCode, new StubKeyModifiers());
  }

  public void keyUp(int keyCode, StubKeyModifiers mods) {
    fireEvent(new StubKeyUpEvent(keyCode, mods));
  }

  public void keyDown(int keyCode) {
    keyDown(keyCode, new StubKeyModifiers());
  }

  public void keyDown(int keyCode, StubKeyModifiers mods) {
    fireEvent(new StubKeyDownEvent(keyCode, mods));
  }

  public void keyPress(char c) {
    keyPress(c, new StubKeyModifiers());
  }

  public void keyPress(char c, StubKeyModifiers mods) {
    fireEvent(new StubKeyPressEvent(c, mods));
  }

  /** Fires down/press/up events for each char. */
  public void press(String chars) {
    for (int i = 0; i < chars.length(); i++) {
      downPressUp(chars.charAt(i));
    }
  }

  /** Fires down/press/up events for each charCode. */
  public void press(char charCode) {
    downPressUp(charCode);
  }

  /** Fires down/up events for keyCode. */
  public void press(int keyCode) {
    downUp(keyCode);
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

  @Override
  public void addFocusListener(FocusListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeFocusListener(FocusListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addKeyboardListener(KeyboardListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeKeyboardListener(KeyboardListener listener) {
    throw new UnsupportedOperationException();
  }

  // Internal method to do down/press/up, so that StubTextBoxBase can
  // override press to also do change events.
  protected void downPressUp(char charCode) {
    int keyCode = StubKeyCodeMapping.map(charCode);
    keyDown(keyCode);
    keyPress(charCode);
    keyUp(keyCode);
  }

  // Internal method to do down/up, so that StubTextBoxBase can override
  // press to also do change events.
  protected void downUp(int keyCode) {
    keyDown(keyCode);
    // keyPress is not useful with key codes anyway
    keyUp(keyCode);
  }

  @Override
  public HandlerRegistration addDragEndHandler(DragEndHandler handler) {
    return handlers.addHandler(DragEndEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragEnterHandler(DragEnterHandler handler) {
    return handlers.addHandler(DragEnterEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler) {
    return handlers.addHandler(DragLeaveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragHandler(DragHandler handler) {
    return handlers.addHandler(DragEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragOverHandler(DragOverHandler handler) {
    return handlers.addHandler(DragOverEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDragStartHandler(DragStartHandler handler) {
    return handlers.addHandler(DragStartEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addDropHandler(DropHandler handler) {
    return handlers.addHandler(DropEvent.getType(), handler);
  }

}
