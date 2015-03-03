package org.tessell.model.properties;

import org.tessell.model.values.Value;

public class IntegerProperty extends AbstractProperty<Integer, IntegerProperty> {

  private Property<String> asString = null;

  public IntegerProperty(final Value<Integer> value) {
    super(value);
  }

  @Override
  protected IntegerProperty getThis() {
    return this;
  }

  @Override
  public Property<String> asString() {
    return asString(getName() + " must be an integer");
  }

  public Property<String> asString(String invalidMessage) {
    if (asString == null) {
      asString = formatted(invalidMessage, new PropertyFormatter<Integer, String>() {
        public String format(Integer a) {
          return Integer.toString(a);
        }

        @Override
        public Integer parse(String b) {
          return Integer.parseInt(b);
        }
      });
    }
    return asString;
  }
}
