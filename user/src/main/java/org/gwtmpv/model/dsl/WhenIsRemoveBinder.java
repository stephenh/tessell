package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsRemoveBinder<P, V> {

  private final Property<P> property;
  private final P value;
  private final V newValue;

  public WhenIsRemoveBinder(Property<P> property, P value, final V newValue) {
    this.property = property;
    this.value = value;
    this.newValue = newValue;
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public HandlerRegistrations from(final ArrayList<V> values) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    update(values); // set initial value
    return hr;
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public HandlerRegistrations from(final ListProperty<V> values) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    update(values); // set initial value
    return hr;
  }

  private void update(ArrayList<V> values) {
    if (eq(value, property.get())) {
      values.remove(newValue);
    } else if (!values.contains(newValue)) {
      values.add(newValue);
    }
  }

  private void update(ListProperty<V> values) {
    if (eq(value, property.get())) {
      values.remove(newValue);
    } else if (!values.get().contains(newValue)) {
      values.add(newValue);
    }
  }

}
