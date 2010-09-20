package org.gwtmpv.model.properties;

import org.gwtmpv.model.values.Value;

public class IntegerProperty extends AbstractStringableProperty<Integer, IntegerProperty> {

  public IntegerProperty(final Value<Integer> value) {
    super(value);
  }

  @Override
  protected IntegerProperty getThis() {
    return this;
  }

  @Override
  protected Integer fromFailableString(final String value) {
    return Integer.valueOf(value);
  }

  @Override
  protected String getAsNonNullString() {
    return String.valueOf(get().intValue());
  }

  @Override
  public String toUserString() {
    return getAsString();
  }

}
