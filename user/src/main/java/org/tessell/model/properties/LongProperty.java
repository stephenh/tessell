package org.tessell.model.properties;

import org.tessell.model.values.Value;

public class LongProperty extends AbstractProperty<Long, LongProperty> {

  public LongProperty(final Value<Long> value) {
    super(value);
  }

  @Override
  protected LongProperty getThis() {
    return this;
  }

  public Property<String> asString() {
    return formatted(new PropertyFormatter<Long, String>() {
      public String format(Long a) {
        return Long.toString(a);
      }

      @Override
      public Long parse(String b) {
        return Long.parseLong(b);
      }
    });
  }
}
