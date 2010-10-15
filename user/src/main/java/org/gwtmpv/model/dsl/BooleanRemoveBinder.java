package org.gwtmpv.model.dsl;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.model.properties.Property;

/** Sets the style based on the property value. */
public class BooleanRemoveBinder<V> {

  private final Binder binder;
  private final Property<Boolean> property;
  private final V value;

  public BooleanRemoveBinder(final Binder binder, Property<Boolean> property, final V value) {
    this.binder = binder;
    this.property = property;
    this.value = value;
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public void from(final ArrayList<V> values) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        update(values);
      }
    }));
    update(values); // set initial value
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public void from(final ListProperty<V> values) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        update(values);
      }
    }));
    update(values); // set initial value
  }

  private void update(ArrayList<V> values) {
    if (Boolean.TRUE.equals(property.get())) {
      values.remove(value);
    } else if (!values.contains(value)) {
      values.add(value);
    }
  }

  private void update(ListProperty<V> values) {
    if (Boolean.TRUE.equals(property.get())) {
      values.remove(value);
    } else if (!values.get().contains(value)) {
      values.add(value);
    }
  }

}
