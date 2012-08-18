package org.tessell.model.dsl;

import java.util.List;

import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;

abstract class AbstractEventBinder {

  /** @return a fluent builder to set {@code property} when triggered. */
  public <P> SetPropertyBinder<P> set(Property<P> property) {
    return new SetPropertyBinder<P>(property, new SetPropertyBinder.Setup() {
      public HandlerRegistrations setup(final Runnable runnable) {
        return new HandlerRegistrations(hookUpRunnable(runnable));
      }
    });
  }

  /** Toggles {@code property} each time the event is triggered. */
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

  /** Focuses on {@code focusable} when triggered. */
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

  /** @return a fluent binder to add {@code value} to a list when triggered. */
  public <P> AddBinder<P> add(P value) {
    return new AddBinder<P>(value);
  }

  /** @return a fluent binder to remove {@code value} remove a list when triggered. */
  public <P> RemoveBinder<P> remove(P value) {
    return new RemoveBinder<P>(value);
  }

  protected abstract HandlerRegistration hookUpRunnable(Runnable runnable);

  protected abstract HandlerRegistration hookUpEventRunnable(DomEventRunnable runnable);

  protected interface DomEventRunnable {
    public void run(DomEvent<?> event);
  }

  public class AddBinder<P> {
    private final P value;

    private AddBinder(P value) {
      this.value = value;
    }

    public HandlerRegistration to(final List<P> values) {
      return hookUpRunnable(new Runnable() {
        public void run() {
          values.add(value);
        }
      });
    }

    public HandlerRegistration to(final ListProperty<P> values) {
      return hookUpRunnable(new Runnable() {
        public void run() {
          values.add(value);
        }
      });
    }
  }

  public class RemoveBinder<P> {
    private final P value;

    private RemoveBinder(P value) {
      this.value = value;
    }

    public HandlerRegistration from(final List<P> values) {
      return hookUpRunnable(new Runnable() {
        public void run() {
          values.remove(value);
        }
      });
    }

    public HandlerRegistration from(final ListProperty<P> values) {
      return hookUpRunnable(new Runnable() {
        public void run() {
          values.remove(value);
        }
      });
    }
  }

}
