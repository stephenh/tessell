package org.tessell.model.dsl;

import static org.tessell.util.ObjectUtils.eq;

import org.tessell.model.properties.Property;

public class WhenConditions {

  private static final WhenCondition<Object> notNull = new WhenCondition<Object>() {
    public boolean evaluate(Property<Object> property) {
      return property.get() != null;
    }

    public void setInitialValue(Property<Object> property) {
    }
  };

  private static final WhenCondition<Object> nullValue = new WhenCondition<Object>() {
    public boolean evaluate(Property<Object> property) {
      return property.get() == null;
    }

    public void setInitialValue(Property<Object> property) {
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

      public void setInitialValue(Property<Integer> property) {
      }
    };
  }

  public static WhenCondition<Integer> lessThan(final Integer number) {
    return new WhenCondition<Integer>() {
      public boolean evaluate(Property<Integer> property) {
        return number != null && property.get() != null && property.get().intValue() < number.intValue();
      }

      public void setInitialValue(Property<Integer> property) {
      }
    };
  }

  public static <P> WhenCondition<P> or(final P... values) {
    return new WhenCondition<P>() {
      public boolean evaluate(Property<P> property) {
        for (P value : values) {
          if (eq(property.get(), value)) {
            return true;
          }
        }
        return false;
      }

      public void setInitialValue(Property<P> property) {
      }
    };
  }
}
