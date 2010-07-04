package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
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
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubHasAllMouseHandlers implements HasAllMouseHandlers {

  private final HandlerManager manager = new HandlerManager(this);

  @Override
  public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
    return manager.addHandler(MouseDownEvent.getType(), handler);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    manager.fireEvent(event);
  }

  @Override
  public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
    return manager.addHandler(MouseUpEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
    return manager.addHandler(MouseOutEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
    return manager.addHandler(MouseOverEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
    return manager.addHandler(MouseMoveEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
    return manager.addHandler(MouseWheelEvent.getType(), handler);
  }

}
