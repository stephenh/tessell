package org.tessell.model.dsl;

import java.util.List;

import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsRemoveBinder<P, V> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final V newValue;

  public WhenIsRemoveBinder(Binder b, Property<P> property, WhenCondition<P> condition, V newValue) {
    this.b = b;
    this.property = property;
    this.newValue = newValue;
    this.condition = condition;
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public void from(final List<V> values) {
    b.add(property.addPropertyChangedHandler(e -> update(values)));
    if (b.canSetInitialValue(property) && !values.contains(newValue)) {
      condition.setInitialValue(property);
    } else {
      update(values); // set initial value
    }
  }

  /** Adds/removes our {@code value} when our property is {@code true}. */
  public void from(final ListProperty<V> values) {
    b.add(property.addPropertyChangedHandler(e -> update(values)));
    if (b.canSetInitialValue(property) && !values.get().contains(newValue)) {
      condition.setInitialValue(property);
    } else {
      update(values); // set initial value
    }
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
