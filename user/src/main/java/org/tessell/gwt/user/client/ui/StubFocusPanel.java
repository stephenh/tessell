package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.StubClickEvent;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubFocusPanel extends StubSimplePanel implements IsFocusPanel {

  public void mouseOver() {
    fireEvent(new DummyMouseOverEvent());
  }

  public void mouseOut() {
    fireEvent(new DummyMouseOutEvent());
  }

  public void click() {
    fireEvent(new StubClickEvent());
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
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
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
  public HandlerRegistration addFocusHandler(final FocusHandler handler) {
    return handlers.addHandler(FocusEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addBlurHandler(final BlurHandler handler) {
    return handlers.addHandler(BlurEvent.getType(), handler);
  }

  private class DummyMouseOverEvent extends MouseOverEvent {
  }

  private class DummyMouseOutEvent extends MouseOutEvent {
  }
}
