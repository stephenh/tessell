package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.StubClickEvent;
import org.tessell.gwt.dom.client.StubDoubleClickEvent;
import org.tessell.gwt.dom.client.StubMouseOutEvent;
import org.tessell.gwt.dom.client.StubMouseOverEvent;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubLabel extends StubWidget implements IsLabel {

  private String text = "";
  private Direction direction;
  private HorizontalAlignmentConstant align;
  private boolean wordWrap;

  public void click() {
    fireEvent(new StubClickEvent());
  }

  public void doubleClick() {
    fireEvent(new StubDoubleClickEvent());
  }

  public void mouseOver() {
    fireEvent(new StubMouseOverEvent());
  }

  public void mouseOut() {
    fireEvent(new StubMouseOutEvent());
  }

  @Override
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return align;
  }

  @Override
  public void setHorizontalAlignment(final HorizontalAlignmentConstant align) {
    this.align = align;
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
    return wordWrap;
  }

  @Override
  public void setWordWrap(final boolean wrap) {
    wordWrap = wrap;
  }

  @Override
  public Direction getDirection() {
    return direction;
  }

  @Override
  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
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
  public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
    return handlers.addHandler(DoubleClickEvent.getType(), handler);
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
