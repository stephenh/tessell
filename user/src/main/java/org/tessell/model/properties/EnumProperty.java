package org.tessell.model.properties;

import org.tessell.model.values.Value;

public class EnumProperty<E extends Enum<E>> extends AbstractProperty<E, EnumProperty<E>> {

  public EnumProperty(final Value<E> value) {
    super(value);
  }

  @Override
  protected EnumProperty<E> getThis() {
    return this;
  }
}
