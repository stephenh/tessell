package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.StubClickEvent;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;

public class StubImage extends StubWidget implements IsImage {

  private String url;

  public void click() {
    fireEvent(new StubClickEvent());
  }

  @Override
  public HandlerRegistration addLoadHandler(final LoadHandler handler) {
    return handlers.addHandler(LoadEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addErrorHandler(final ErrorHandler handler) {
    return handlers.addHandler(ErrorEvent.getType(), handler);
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
  public void setUrl(final String url) {
    this.url = url;
  }

  @Override
  public void setUrl(SafeUri url) {
    setUrl(url.asString());
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public void setResource(ImageResource imageResource) {
    setUrl(imageResource.getSafeUri().asString());
  }

}
