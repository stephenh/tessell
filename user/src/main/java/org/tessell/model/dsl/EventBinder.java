package org.tessell.model.dsl;

import static java.lang.Boolean.TRUE;

import java.util.List;

import org.tessell.model.commands.UiCommand;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;
import org.tessell.place.PlaceRequest;
import org.tessell.place.events.PlaceRequestEvent;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.web.bindery.event.shared.EventBus;

public abstract class EventBinder {

  private final Binder b;

  protected EventBinder(final Binder b) {
    this.b = b;
  }

  /** @return a fluent builder to set {@code property} when triggered. */
  public <P> SetPropertyBinder<P> set(Property<P> property) {
    return new SetPropertyBinder<P>(b, property, new SetPropertyBinder.Setup() {
      public HandlerRegistration setup(final Runnable runnable) {
        return hookUpRunnable(runnable);
      }
    });
  }

  /** Toggles {@code property} each time the event is triggered. */
  public void toggle(final Property<Boolean> property) {
    b.add(hookUpEventRunnable(new DomEventRunnable() {
      public void run(DomEvent<?> event) {
        property.set(TRUE.equals(property.get()) ? false : true);
        if (event != null) {
          event.preventDefault();
        }
      }
    }));
  }

  /** Focuses on {@code focusable} when triggered. */
  public void focus(final Focusable focusable) {
    b.add(hookUpEventRunnable(new DomEventRunnable() {
      public void run(DomEvent<?> event) {
        focusable.setFocus(true);
        if (event != null) {
          event.preventDefault();
        }
      }
    }));
  }

  /** Cancels the event's default action. */
  public void preventDefault() {
    b.add(hookUpEventRunnable(new DomEventRunnable() {
      public void run(DomEvent<?> event) {
        event.preventDefault();
      }
    }));
  }

  /** Executes {@code command} when triggered. */
  public void execute(final UiCommand command) {
    b.add(hookUpRunnable(new Runnable() {
      public void run() {
        command.execute();
      }
    }));
  }

  /** Executes {@code runnable} when triggered. */
  public void execute(final Runnable runnable) {
    b.add(hookUpRunnable(runnable));
  }

  /** Fires a {@link PlaceRequestEvent} for {@code request} when triggered. */
  public void goTo(final EventBus bus, final PlaceRequest request) {
    b.add(hookUpRunnable(new Runnable() {
      public void run() {
        bus.fireEvent(request.asEvent());
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

    public void to(final List<P> values) {
      b.add(hookUpRunnable(new Runnable() {
        public void run() {
          values.add(value);
        }
      }));
    }

    public void to(final ListProperty<P> values) {
      b.add(hookUpRunnable(new Runnable() {
        public void run() {
          values.add(value);
        }
      }));
    }
  }

  public class RemoveBinder<P> {
    private final P value;

    private RemoveBinder(P value) {
      this.value = value;
    }

    public void from(final List<P> values) {
      b.add(hookUpRunnable(new Runnable() {
        public void run() {
          values.remove(value);
        }
      }));
    }

    public void from(final ListProperty<P> values) {
      b.add(hookUpRunnable(new Runnable() {
        public void run() {
          values.remove(value);
        }
      }));
    }
  }

}
