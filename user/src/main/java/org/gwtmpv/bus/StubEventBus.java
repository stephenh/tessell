package org.gwtmpv.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class StubEventBus extends EventBus {

  private final EventBus m = new SimpleEventBus();
  private static final Logger log = Logger.getLogger(StubEventBus.class.getName());
  private final List<GwtEvent<?>> events = new ArrayList<GwtEvent<?>>();

  @Override
  public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
    return m.addHandler(type, handler);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    log.fine(event.toString());
    events.add(event);
    m.fireEvent(event);
  }

  @Override
  public <H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
    return m.addHandlerToSource(type, source, handler);
  }

  @Override
  public void fireEventFromSource(GwtEvent<?> event, Object source) {
    log.fine(event.toString());
    events.add(event);
    m.fireEventFromSource(event, source);
  }

  @SuppressWarnings("unchecked")
  public <E extends GwtEvent<?>> List<E> getEvents(final Class<E> type) {
    final List<E> matched = new ArrayList<E>();
    for (final GwtEvent<?> event : events) {
      if (event.getClass().equals(type)) {
        matched.add((E) event);
      }
    }
    return matched;
  }

  public <E extends GwtEvent<?>> E getEvent(final Class<E> type, final int i) {
    return getEvents(type).get(i);
  }

}
