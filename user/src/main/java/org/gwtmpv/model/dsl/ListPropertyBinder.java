package org.gwtmpv.model.dsl;

import java.util.HashMap;
import java.util.Map;

import org.gwtmpv.model.events.ValueAddedEvent;
import org.gwtmpv.model.events.ValueAddedHandler;
import org.gwtmpv.model.events.ValueRemovedEvent;
import org.gwtmpv.model.events.ValueRemovedHandler;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.presenter.Presenter;
import org.gwtmpv.widgets.IsPanel;
import org.gwtmpv.widgets.IsWidget;

/** Fluent binding methods for {@link ListProperty}s. */
public class ListPropertyBinder<P> {

  public interface ListViewFactory<P> {
    IsWidget create(P value);
  }

  public interface ListPresenterFactory<P> {
    Presenter create(P value);
  }

  private final Binder binder;
  private final ListProperty<P> p;

  public ListPropertyBinder(Binder binder, ListProperty<P> p) {
    this.binder = binder;
    this.p = p;
  }

  /** Binds each value in {@code p} to a view created by {@code factory}. */
  public void to(final IsPanel panel, final ListViewFactory<P> factory) {
    // map to remember the model->view mapping so we know which view to remove later
    final Map<P, IsWidget> views = new HashMap<P, IsWidget>();
    for (P value : p.get()) {
      IsWidget view = factory.create(value);
      views.put(value, view);
      panel.add(view);
    }
    binder.registerHandler(p.addValueAddedHandler(new ValueAddedHandler<P>() {
      public void onValueAdded(ValueAddedEvent<P> event) {
        IsWidget view = factory.create(event.getValue());
        views.put(event.getValue(), view);
        panel.add(view);
      }
    }));
    binder.registerHandler(p.addValueRemovedHandler(new ValueRemovedHandler<P>() {
      public void onValueRemoved(ValueRemovedEvent<P> event) {
        IsWidget view = views.remove(event.getValue());
        if (view != null) {
          panel.remove(view);
        }
      }
    }));
  }

  /** Binds each value in {@code p} to a presenter created by {@code factory}.
   *
   * Also adds/removes the child presenters to the {@code parent} presenter for proper binding/unbinding.
   */
  public void to(final Presenter parent, final IsPanel panel, final ListPresenterFactory<P> factory) {
    // map to remember the model->presenter mapping so we know which view to remove later
    final Map<P, Presenter> views = new HashMap<P, Presenter>();
    for (P value : p.get()) {
      Presenter child = factory.create(value);
      views.put(value, child);
      panel.add(child.getView());
    }
    binder.registerHandler(p.addValueAddedHandler(new ValueAddedHandler<P>() {
      public void onValueAdded(ValueAddedEvent<P> event) {
        Presenter child = factory.create(event.getValue());
        views.put(event.getValue(), child);
        panel.add(child.getView());
      }
    }));
    binder.registerHandler(p.addValueRemovedHandler(new ValueRemovedHandler<P>() {
      public void onValueRemoved(ValueRemovedEvent<P> event) {
        Presenter view = views.remove(event.getValue());
        if (view != null) {
          panel.remove(view.getView());
        }
      }
    }));
  }

}
