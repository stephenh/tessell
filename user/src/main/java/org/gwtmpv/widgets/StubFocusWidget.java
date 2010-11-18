package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubFocusWidget extends StubWidget implements IsFocusWidget {

  private static StubFocusWidget lastFocus;
  private boolean enabled = true;
  private boolean focus;

  public void click() {
    // is only-click if enabled true for all FocusWidgets?
    if (!enabled) {
      throw new IllegalStateException(this + " is disabled");
    }
    fireEvent(new DummyClickEvent());
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
      fireEvent(new DummyFocusEvent());
      lastFocus = this;
    } else {
      focus = false;
      blur();
    }
  }

  public void blur() {
    fireEvent(new DummyBlurEvent());
  }

  public void mouseOver() {
    fireEvent(new DummyMouseOverEvent());
  }

  public void mouseOut() {
    fireEvent(new DummyMouseOutEvent());
  }

  public void keyUp() {
    fireEvent(new DummyKeyUpEvent(0));
  }

  public void keyUp(int keyCode) {
    fireEvent(new DummyKeyUpEvent(keyCode));
  }

  public void keyDown() {
    fireEvent(new DummyKeyDownEvent());
  }

  public void keyPress() {
    fireEvent(new DummyKeyPressEvent());
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
  }

  @Override
  public int getTabIndex() {
    throw new UnsupportedOperationException("This is a stub.");
  }

  @Override
  public void setAccessKey(final char key) {
    throw new UnsupportedOperationException("This is a stub.");

  }

  @Override
  public void setTabIndex(final int index) {
    throw new UnsupportedOperationException("This is a stub.");

  }

  @Override
  public HandlerRegistration addFocusHandler(final FocusHandler handler) {
    throw new UnsupportedOperationException("This is a stub.");
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

  public static class DummyClickEvent extends ClickEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyMouseOverEvent extends MouseOverEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyMouseOutEvent extends MouseOutEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyBlurEvent extends BlurEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyFocusEvent extends FocusEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyKeyDownEvent extends KeyDownEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyKeyPressEvent extends KeyPressEvent {
    public boolean prevented = false;

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

  public static class DummyKeyUpEvent extends KeyUpEvent {
    public boolean prevented = false;
    private final int keyCode;

    public DummyKeyUpEvent(int keyCode) {
      this.keyCode = keyCode;
    }

    @Override
    public int getNativeKeyCode() {
      return keyCode;
    }

    @Override
    public void preventDefault() {
      prevented = true;
    }
  }

}
