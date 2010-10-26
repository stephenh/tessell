package org.gwtmpv.model.values;

import org.gwtmpv.model.properties.Property;

/**
 * Wraps a simple value as a {@link Value}.
 * 
 * This is for making {@link Property}s around your own adhoc properties.
 */
public class SetValue<P> implements Value<P> {

  private final String name;
  private P value;

  public SetValue(final String name) {
    this.name = name;
  }

  public SetValue(final String name, P initialValue) {
    this.name = name;
    value = initialValue;
  }

  @Override
  public P get() {
    return value;
  }

  @Override
  public void set(final P value) {
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

}
