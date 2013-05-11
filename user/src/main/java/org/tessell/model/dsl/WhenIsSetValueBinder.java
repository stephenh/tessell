package org.tessell.model.dsl;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;

public class WhenIsSetValueBinder<P, Q> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final TakesValue<Q> value;

  WhenIsSetValueBinder(final Binder b, final Property<P> property, final WhenCondition<P> condition, final TakesValue<Q> value) {
    this.b = b;
    this.property = property;
    this.condition = condition;
    this.value = value;
  }

  public void to(final Q newValue) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        if (condition.evaluate(property)) {
          value.setValue(newValue);
        }
      }
    }));
    if (condition.evaluate(property)) {
      value.setValue(newValue); // set initial
    }
  }

  public void to(final HasValue<Q> newValue) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        if (condition.evaluate(property)) {
          value.setValue(newValue.getValue());
        }
      }
    }));
    if (condition.evaluate(property)) {
      value.setValue(newValue.getValue()); // set initial
    }
  }

  public void toOrElse(final Q ifTrue, final Q ifFalse) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        update(ifTrue, ifFalse);
      }
    }));
    // set initial value
    update(ifTrue, ifFalse);
  }

  private void update(final Q ifTrue, final Q ifFalse) {
    if (condition.evaluate(property)) {
      value.setValue(ifTrue);
    } else {
      value.setValue(ifFalse);
    }
  }

}
