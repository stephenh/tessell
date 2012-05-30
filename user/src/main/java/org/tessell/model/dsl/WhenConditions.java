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

  public static WhenCondition<Integer> greaterThan(final Integer number) {
    return new WhenCondition<Integer>() {
      public boolean evaluate(Property<Integer> property) {
        return number != null && property.get() != null && property.get().intValue() > number.intValue();
      }
    };
  }

  public static WhenCondition<Integer> lessThan(final Integer number) {
    return new WhenCondition<Integer>() {
      public boolean evaluate(Property<Integer> property) {
        return number != null && property.get() != null && property.get().intValue() < number.intValue();
      }
    };
  }
}
