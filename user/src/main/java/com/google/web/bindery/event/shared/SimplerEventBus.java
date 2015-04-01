/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.web.bindery.event.shared;

import java.util.*;

import com.google.web.bindery.event.shared.Event.Type;

/**
 * Simpler implementation of {@link EventBus}.
 *
 * Specifically, we:
 *
 * 1. Don't worry about legacy HandlerManager features (reverse firing)
 *
 * 2. Adds/removes immediately take effect, even if an event is already firing
 *
 * The 2nd difference is the most critical to me, where something like place
 * changed firing (so firingDepth is already > 0) leads to something wanting
 * to listen to events right away, and not wait until the place change is
 * finished.
 */
public class SimplerEventBus extends EventBus {

  private boolean isFiring;
  private int doFireEntries;
  private String doFireDebugging = "";

  /** Handler lists that have null markers in them. */
  private final List<ToClean<?>> needsCleaning = new ArrayList<ToClean<?>>();

  /** Map of event type to map of event source to list of their handlers. */
  private final Map<Event.Type<?>, Map<Object, List<?>>> map = new HashMap<Event.Type<?>, Map<Object, List<?>>>();

  /** Queued events. */
  private final List<ToFire> queuedEvents = new ArrayList<ToFire>();

  @Override
  public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
    checkNotNull(type, "Cannot add a handler with a null type");
    checkNotNull(handler, "Cannot add a null handler");
    return doAdd(type, null, handler);
  }

  @Override
  public <H> HandlerRegistration addHandlerToSource(final Event.Type<H> type, final Object source, final H handler) {
    checkNotNull(type, "Cannot add a handler with a null type");
    checkNotNull(source, "Cannot add a handler with a null source");
    checkNotNull(handler, "Cannot add a null handler");
    return doAdd(type, source, handler);
  }

  @Override
  public void fireEvent(Event<?> event) {
    checkNotNull(event, "Cannot fire null event");
    doFire(event, null);
  }

  @Override
  public void fireEventFromSource(Event<?> event, Object source) {
    checkNotNull(event, "Cannot fire null event");
    checkNotNull(source, "Cannot fire from a null source");
    doFire(event, source);
  }

  /** Creates new map entries for {@code type}/{@code source} and adds {@code handler}. */
  private <H> HandlerRegistration doAdd(final Event.Type<H> type, final Object source, final H handler) {
    ensureHandlerList(type, source).add(handler);
    return new HandlerRegistration() {
      public void removeHandler() {
        if (isFiring) {
          doRemoveWithDeferredCleanup(type, source, handler);
        } else {
          doRemoveNow(type, source, handler);
        }
      }
    };
  }

  private <H> void doFire(Event<H> event, Object source) {
    if (isFiring) {
      if (doFireEntries > 50) {
        doFireDebugging += "Queue " + event.toDebugString() + ", ";
      }
      queuedEvents.add(new ToFire(event, source));
      return;
    }
    // Sanity check against recursion
    doFireEntries++;
    try {
      isFiring = true;

      // Start debugging around 50 re-entries
      if (doFireEntries > 50) {
        doFireDebugging += "Fire " + event.toDebugString() + ", ";
      }
      // After 55 re-entries, give up
      if (doFireEntries > 55) {
        throw new IllegalStateException("Detected loop: " + doFireDebugging);
      }

      if (source != null) {
        event.setSource(source);
      }

      Set<Throwable> causes = null;

      List<H> handlers = getDispatchList(event.getAssociatedType(), source);
      for (int i = 0; i < handlers.size(); i++) {
        H handler = handlers.get(i);
        // was the handler unregistered during our iteration?
        if (handler == null) {
          continue;
        }
        try {
          event.dispatch(handler);
        } catch (Throwable e) {
          if (causes == null) {
            causes = new HashSet<Throwable>();
          }
          causes.add(e);
        }
      }

      if (!queuedEvents.isEmpty()) {
        isFiring = false;
        try {
          fireQueuedEvents();
        } catch (UmbrellaException e) {
          if (causes == null) {
            causes = new HashSet<Throwable>();
          }
          causes.addAll(e.getCauses());
        }
      }

      if (causes != null) {
        throw new UmbrellaException(causes);
      }
    } finally {
      isFiring = false;
      doFireEntries--;
      executeCleaning();
    }
  }

  private <H> void doRemoveNow(final Event.Type<H> type, final Object source, final H handler) {
    List<H> l = getHandlerList(type, source);
    if (l.remove(handler) && l.isEmpty()) {
      prune(type, source);
    }
  }

  private <H> void doRemoveWithDeferredCleanup(final Event.Type<H> type, final Object source, final H handler) {
    // immediately mark the handler as removed
    final List<H> l = getHandlerList(type, source);
    final int handlerIndex = l.indexOf(handler);
    assert handlerIndex >= 0 : "handler was not registered " + handler;
    l.set(handlerIndex, null);
    // defer the cleanup of the null marker
    needsCleaning.add(new ToClean<H>(type, source, l));
  }

  private <H> void clean(ToClean<H> toClean) {
    toClean.l.remove(null);
    if (toClean.l.isEmpty()) {
      prune(toClean.type, toClean.source);
    }
  }

  private <H> List<H> getDispatchList(Event.Type<H> type, Object source) {
    if (source == null) {
      return getHandlerList(type, null);
    } else {
      List<H> all = new ArrayList<H>();
      all.addAll(getHandlerList(type, source));
      all.addAll(getHandlerList(type, null));
      return all;
    }
  }

  /** @return handlers for {@code type}/{@code source}, has no side-effects if none are registered yet. */
  private <H> List<H> getHandlerList(Event.Type<H> type, Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      return Collections.emptyList();
    }
    // safe, we control the puts.
    @SuppressWarnings("unchecked")
    List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      return Collections.emptyList();
    }
    return handlers;
  }

  /** @return handlers for {@code type}/{@code source}, creates the list if this is the first registration. */
  private <H> List<H> ensureHandlerList(Event.Type<H> type, Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      sourceMap = new HashMap<Object, List<?>>();
      map.put(type, sourceMap);
    }
    // safe, we control the puts.
    @SuppressWarnings("unchecked")
    List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      handlers = new ArrayList<H>();
      sourceMap.put(source, handlers);
    }
    return handlers;
  }

  private void executeCleaning() {
    for (ToClean<?> h : needsCleaning) {
      clean(h);
    }
    needsCleaning.clear();
  }

  private void fireQueuedEvents() {
    while (!queuedEvents.isEmpty()) {
      ToFire f = queuedEvents.remove(0);
      doFire(f.event, f.source);
    }
  }

  private void prune(Event.Type<?> type, Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    List<?> pruned = sourceMap.remove(source);
    assert pruned != null : "Can't prune what wasn't there";
    assert pruned.isEmpty() : "Pruned unempty list!";
    if (sourceMap.isEmpty()) {
      map.remove(type);
    }
  }

  private void checkNotNull(Object arg, String message) {
    if (arg == null) {
      throw new NullPointerException(message);
    }
  }

  private static class ToFire {
    private final Event<?> event;
    private final Object source;

    private ToFire(Event<?> event, Object source) {
      this.event = event;
      this.source = source;
    }
  }

  private static class ToClean<H> {
    private final Event.Type<H> type;
    private final Object source;
    private final List<H> l;

    private ToClean(Event.Type<H> type, Object source, List<H> l) {
      this.type = type;
      this.source = source;
      this.l = l;
    }
  }

}
