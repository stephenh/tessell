package org.tessell.model.dsl;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

import com.google.gwt.user.client.TakesValue;

public class WhenIsSetValueBinder<P, Q> {

  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final TakesValue<Q> value;

  WhenIsSetValueBinder(final Property<P> property, final WhenCondition<P> condition, final TakesValue<Q> value) {
    this.property = property;
    this.condition = condition;
    this.value = value;
  }

  public HandlerRegistrations to(final Q newValue) {
    final HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        if (condition.evaluate(property)) {
          value.setValue(newValue);
        }
      }
    }));
    if (condition.evaluate(property)) {
      // set initial
      value.setValue(newValue);
    }
    return hr;
  }

  public HandlerRegistrations toOrElse(final Q ifTrue, final Q ifFalse) {
    final HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        update(ifTrue, ifFalse);
      }
    }));
    // set initial value
    update(ifTrue, ifFalse);
    return hr;
  }

  private void update(final Q ifTrue, final Q ifFalse) {
    if (condition.evaluate(property)) {
      value.setValue(ifTrue);
    } else {
      value.setValue(ifFalse);
    }
  }

}
