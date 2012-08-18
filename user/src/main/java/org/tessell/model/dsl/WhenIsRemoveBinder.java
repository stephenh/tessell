package org.tessell.model.dsl;

import java.util.List;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsRemoveBinder<P, V> {

  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final V newValue;

  public WhenIsRemoveBinder(Property<P> property, WhenCondition<P> condition, V newValue) {
    this.property = property;
    this.newValue = newValue;
    this.condition = condition;
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public HandlerRegistrations from(final List<V> values) {
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

  private void update(List<V> values) {
    if (condition.evaluate(property)) {
      values.remove(newValue);
    } else if (!values.contains(newValue)) {
      values.add(newValue);
    }
  }

  private void update(ListProperty<V> values) {
    if (condition.evaluate(property)) {
      values.remove(newValue);
    } else if (!values.get().contains(newValue)) {
      values.add(newValue);
    }
  }

}
