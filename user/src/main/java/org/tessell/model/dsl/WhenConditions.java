package org.tessell.model.dsl;

import static org.tessell.util.ObjectUtils.eq;

import org.tessell.model.properties.Property;

/**
 * Common conditions.
 *
 * These can be used either in the Binder DSL (see {@link WhenBinder#is(WhenCondition)})
 * or against properties directly (see {@link Property#is(org.tessell.model.properties.Condition)}).
 */
public class WhenConditions {

  private static final WhenCondition<Object> notNull = new WhenCondition<Object>() {
    public boolean evaluate(Object value) {
      return value != null;
    }

  };

  private static final WhenCondition<Object> nullValue = new WhenCondition<Object>() {
    public boolean evaluate(Object value) {
      return value == null;
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
      public boolean evaluate(Integer value) {
        return number != null && value != null && value.intValue() > number.intValue();
      }
    };
  }

  public static WhenCondition<Integer> lessThan(final Integer number) {
    return new WhenCondition<Integer>() {
      public boolean evaluate(Integer value) {
        return number != null && value != null && value.intValue() < number.intValue();
      }
    };
  }

  public static <P> WhenCondition<P> or(final P... values) {
    return new WhenCondition<P>() {
      public boolean evaluate(P currentValue) {
        for (P value : values) {
          if (eq(currentValue, value)) {
            return true;
          }
        }
        return false;
      }
    };
  }
}
