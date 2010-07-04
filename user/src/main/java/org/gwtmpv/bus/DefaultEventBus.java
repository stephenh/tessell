package org.gwtmpv.bus;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.FixedHandlerManager;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;

public class DefaultEventBus implements EventBus {

  private final FixedHandlerManager m = new FixedHandlerManager(this);

  @Override
  public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
    return m.addHandler(type, handler);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    m.fireEvent(event);
  }

}
