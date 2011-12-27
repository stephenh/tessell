package org.tessell.model.dsl;

import org.tessell.model.properties.Property;

public class WhenConditions {

  private static final WhenCondition<Object> notNull = new WhenCondition<Object>() {
    public boolean evaluate(Property<Object> property) {
      return property.get() != null;
    }
  };

  private static final WhenCondition<Object> nullValue = new WhenCondition<Object>() {
    public boolean evaluate(Property<Object> property) {
      return property.get() == null;
    }
  };

  public static WhenCondition<Object> notNull() {
    return notNull;
  }

  public static WhenCondition<Object> nullValue() {
    return nullValue;
  }

}
