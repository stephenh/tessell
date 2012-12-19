package org.tessell.model.properties;

import org.tessell.model.values.Value;

public class IntegerProperty extends AbstractProperty<Integer, IntegerProperty> {

  public IntegerProperty(final Value<Integer> value) {
    super(value);
  }

  @Override
  protected IntegerProperty getThis() {
    return this;
  }

  public Property<String> asString() {
    return asString(getName() + " must be an integer");
  }

  public Property<String> asString(String invalidMessage) {
    return formatted(invalidMessage, new PropertyFormatter<Integer, String>() {
      public String format(Integer a) {
        return Integer.toString(a);
      }

      @Override
      public Integer parse(String b) {
        return Integer.parseInt(b);
      }
    });
  }
}
