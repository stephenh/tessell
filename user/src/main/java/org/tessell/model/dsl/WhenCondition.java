package org.tessell.model.dsl;

import org.tessell.model.properties.Condition;
import org.tessell.model.properties.Property;

/** A condition that can also set the default value. */
public abstract class WhenCondition<P> implements Condition<P> {

  /** A helper method to evaluate this condition against {@code property}'s value> */
  public boolean evaluate(Property<P> property) {
    return evaluate(property.get());
  }

  /** A method for conditions to set an initial value, should a property be unset. */
  public void setInitialValue(Property<P> property) {
  }

}
