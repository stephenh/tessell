package org.gwtmpv.model.properties;

import org.gwtmpv.model.values.Value;

public class EnumProperty<E extends Enum<E>> extends AbstractProperty<E, EnumProperty<E>> {

  public EnumProperty(final Value<E> value) {
    super(value);
  }

  @Override
  protected EnumProperty<E> getThis() {
    return this;
  }
}
