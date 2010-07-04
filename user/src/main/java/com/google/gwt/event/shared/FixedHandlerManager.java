package com.google.gwt.event.shared;

import java.util.ArrayList;
import java.util.HashMap;

/** Allows addition/removal of handlers to be reflected immediately. */
public class FixedHandlerManager {

  private final HashMap<GwtEvent.Type<?>, ArrayList<?>> map = new HashMap<GwtEvent.Type<?>, ArrayList<?>>();
  private final ArrayList<Runnable> deferredRemoveCleanup = new ArrayList<Runnable>();
  private final Object source;
  private int firingDepth = 0;

  public FixedHandlerManager(final Object source) {
    this.source = source;
  }

  /**
   * Adds a handle.
   * 
   * @param <H>
   *          The type of handler
   * @param type
   *          the event type associated with this handler
   * @param handler
   *          the handler
   * @return the handler registration, can be stored in order to remove the handler later
   */
  public <H extends EventHandler> HandlerRegistration addHandler(final GwtEvent.Type<H> type, final H handler) {
    assert type != null : "Cannot add a handler with a null type";
    assert handler != null : "Cannot add a null handler";
    final ArrayList<H> handlers = getOrCreate(type);
    handlers.add(handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        doRemoveHandler(type, handlers, handler);
      }
    };
  }

  /**
   * Fires the given event to the handlers listening to the event's type.
   * 
   * Note, any subclass should be very careful about overriding this method, as adds/removes of handlers will not be
   * safe except within this implementation.
   * 
   * @param event
   *          the event
   */
  public void fireEvent(final GwtEvent<?> event) {
    // If it not live we should revive it.
    if (!event.isLive()) {
      event.revive();
    }
    final Object oldSource = event.getSource();
    event.setSource(source);
    try {
      firingDepth++;
      doFireEvent(event);
    } finally {
      firingDepth--;
      if (firingDepth == 0) {
        for (final Runnable cleanup : deferredRemoveCleanup) {
          cleanup.run();
        }
        deferredRemoveCleanup.clear();
      }
    }
    if (oldSource == null) {
      // This was my event, so I should kill it now that I'm done.
      event.kill();
    } else {
      // Restoring the source for the next handler to use.
      event.setSource(oldSource);
    }
  }

  private <H extends EventHandler> void doFireEvent(final GwtEvent<H> event) {
    final ArrayList<H> l = get(event.getAssociatedType());
    if (l == null) {
      return;
    }
    final int number = l.size();
    for (int i = 0; i < number; i++) {
      final H handler = l.get(i);
      if (handler != null) {
        event.dispatch(handler);
      }
    }
  }

  private <H> void doRemoveHandler(final GwtEvent.Type<H> type, final ArrayList<H> handlers, final H handler) {
    if (firingDepth == 0) {
      handlers.remove(handler);
      if (handlers.size() == 0) {
        map.remove(type);
      }
    } else {
      deferredRemoveCleanup.add(new Runnable() {
        public void run() {
          doRemoveHandler(type, handlers, handler);
        }
      });
    }
  }

  // This annotation is commented out because it NPEs Eclipse 3.6's APT
  // @SuppressWarnings("unchecked")
  private <H> ArrayList<H> get(final GwtEvent.Type<H> type) {
    return (ArrayList<H>) map.get(type); // This cast is safe because we control the puts.
  }

  private <H> ArrayList<H> getOrCreate(final GwtEvent.Type<H> type) {
    ArrayList<H> handlers = get(type);
    if (handlers == null) {
      handlers = new ArrayList<H>();
      map.put(type, handlers);
    }
    return handlers;
  }

}
