package org.tessell.model.dsl;

import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.Property;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;

abstract class AbstractEventBinder {

  public <P> SetPropertyBinder<P> set(Property<P> property) {
    return new SetPropertyBinder<P>(property, new SetPropertyBinder.Setup() {
      public HandlerRegistrations setup(final Runnable runnable) {
        return new HandlerRegistrations(hookUpRunnable(runnable));
      }
    });
  }

  public HandlerRegistrations toggle(final BooleanProperty property) {
    return new HandlerRegistrations(hookUpEventRunnable(new DomEventRunnable() {
      public void run(DomEvent<?> event) {
        property.toggle();
        if (event != null) {
          event.preventDefault();
        }
      }
    }));
  }

  public HandlerRegistrations focus(final Focusable focusable) {
    return new HandlerRegistrations(hookUpEventRunnable(new DomEventRunnable() {
      public void run(DomEvent<?> event) {
        focusable.setFocus(true);
        if (event != null) {
          event.preventDefault();
        }
      }
    }));
  }

  protected abstract HandlerRegistration hookUpRunnable(Runnable runnable);

  protected abstract HandlerRegistration hookUpEventRunnable(DomEventRunnable runnable);

  protected interface DomEventRunnable {
    public void run(DomEvent<?> event);
  }

}
