package org.tessell.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimplerEventBus;

public class StubEventBus extends EventBus {

  private final EventBus m = new SimplerEventBus();
  private static final Logger log = Logger.getLogger(StubEventBus.class.getName());
  private final List<Event<?>> events = new ArrayList<Event<?>>();

  @Override
  public <H> HandlerRegistration addHandler(final Type<H> type, final H handler) {
    return m.addHandler(type, handler);
  }

  @Override
  public void fireEvent(final Event<?> event) {
    log.fine(event.toString());
    events.add(event);
    m.fireEvent(event);
  }

  @Override
  public <H> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
    return m.addHandlerToSource(type, source, handler);
  }

  @Override
  public void fireEventFromSource(Event<?> event, Object source) {
    log.fine(event.toString());
    events.add(event);
    m.fireEventFromSource(event, source);
  }

  public List<Event<?>> getEvents() {
    return events;
  }

  @SuppressWarnings("unchecked")
  public <E extends Event<?>> List<E> getEvents(final Class<E> type) {
    final List<E> matched = new ArrayList<E>();
    for (final Event<?> event : events) {
      if (event.getClass().equals(type)) {
        matched.add((E) event);
      }
    }
    return matched;
  }

  public <E extends Event<?>> E getEvent(final Class<E> type, final int i) {
    return getEvents(type).get(i);
  }

}
