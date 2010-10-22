package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.model.properties.Property;

/** Adds {@code newValue} to a list based on the {@code value} of a {@code property}. */
public class WhenIsAddBinder<P, V> {

  private final Binder binder;
  private final Property<P> property;
  private final P value;
  private final V newValue;

  public WhenIsAddBinder(final Binder binder, Property<P> property, P value, final V newValue) {
    this.binder = binder;
    this.property = property;
    this.value = value;
    this.newValue = newValue;
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public void to(final ArrayList<V> values) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    update(values); // set initial value
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public void to(final ListProperty<V> values) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    update(values); // set initial value
  }

  private void update(ArrayList<V> values) {
    if (eq(value, property.get())) {
      values.add(newValue);
    } else if (values.contains(newValue)) {
      values.remove(newValue);
    }
  }

  private void update(ListProperty<V> values) {
    if (eq(value, property.get())) {
      values.add(newValue);
    } else if (values.get().contains(newValue)) {
      values.remove(newValue);
    }
  }

}
