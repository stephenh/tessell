package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubLabel extends StubWidget implements IsLabel {

  private String text = "";

  public void click() {
    fireEvent(new DummyClickEvent());
  }

  @Override
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return null;
  }

  @Override
  public void setHorizontalAlignment(final HorizontalAlignmentConstant align) {
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(final String text) {
    this.text = text == null ? "" : text;
  }

  @Override
  public boolean getWordWrap() {
    return false;
  }

  @Override
  public void setWordWrap(final boolean wrap) {
  }

  @Override
  public Direction getDirection() {
    return null;
  }

  @Override
  public void setDirection(final Direction direction) {
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
    return null;
  }

  @Override
  public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
    return null;
  }

  @Override
  public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
    return null;
  }

  @Override
  public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
    return null;
  }

  @Override
  public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
    return null;
  }

  @Override
  public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
    return null;
  }

  private class DummyClickEvent extends ClickEvent {
  }

}
