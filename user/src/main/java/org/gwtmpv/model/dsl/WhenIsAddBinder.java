package org.gwtmpv.model.dsl;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.model.properties.Property;

/** Adds {@code newValue} to a list based on the {@code value} of a {@code property}. */
public class WhenIsAddBinder<P, V> {

  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final V newValue;

  public WhenIsAddBinder(Property<P> property, WhenCondition<P> condition, V newValue) {
    this.property = property;
    this.newValue = newValue;
    this.condition = condition;
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public HandlerRegistrations to(final ArrayList<V> values) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    update(values); // set initial value
    return hr;
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public HandlerRegistrations to(final ListProperty<V> values) {
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
    if (condition.evaluate(property)) {
      values.add(newValue);
    } else if (values.contains(newValue)) {
      values.remove(newValue);
    }
  }

  private void update(ListProperty<V> values) {
    if (condition.evaluate(property)) {
      values.add(newValue);
    } else if (values.get().contains(newValue)) {
      values.remove(newValue);
    }
  }
}
