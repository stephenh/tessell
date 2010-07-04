package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubHasClickHandlers implements HasClickHandlers {

  private final HandlerManager handlers = new HandlerManager(this);

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return handlers.addHandler(ClickEvent.getType(), handler);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

  public int getNumberOfClickHandlers() {
    return handlers.getHandlerCount(ClickEvent.getType());
  }

  public void click() {
    fireEvent(new DummyClickEvent());
  }

  private class DummyClickEvent extends ClickEvent {
  }

}
