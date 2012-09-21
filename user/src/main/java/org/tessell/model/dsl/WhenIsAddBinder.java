package org.tessell.model.dsl;

import java.util.List;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;

/** Adds {@code newValue} to a list based on the {@code value} of a {@code property}. */
public class WhenIsAddBinder<P, V> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final V newValue;

  public WhenIsAddBinder(Binder b, Property<P> property, WhenCondition<P> condition, V newValue) {
    this.b = b;
    this.property = property;
    this.newValue = newValue;
    this.condition = condition;
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public void to(final List<V> values) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    if (b.canSetInitialValue(property) && values.contains(newValue)) {
      condition.setInitialValue(property);
    } else {
      update(values); // set initial value
    }
  }

  /** Adds/removes our {@code value} when our {@code property} is {@code value}. */
  public void to(final ListProperty<V> values) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(values);
      }
    }));
    if (b.canSetInitialValue(property) && values.get().contains(newValue)) {
      condition.setInitialValue(property);
    } else {
      update(values); // set initial value
    }
  }

  private void update(List<V> values) {
    if (condition.evaluate(property)) {
      if (!values.contains(newValue)) {
        values.add(newValue);
      }
    } else if (values.contains(newValue)) {
      values.remove(newValue);
    }
  }

  private void update(ListProperty<V> values) {
    if (condition.evaluate(property)) {
      if (!values.get().contains(newValue)) {
        values.add(newValue);
      }
    } else if (values.get().contains(newValue)) {
      values.remove(newValue);
    }
  }
}
