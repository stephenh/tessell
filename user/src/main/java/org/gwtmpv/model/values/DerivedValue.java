package org.gwtmpv.model.values;

import org.gwtmpv.model.properties.Property;

/**
 * Wraps a function as a {@link Value}.
 * 
 * This is for making {@link Property}s around a value you compute with a function.
 */
public abstract class DerivedValue<P> implements Value<P> {

  @Override
  public abstract P get();

  @Override
  public void set(final P value) {
    throw new IllegalStateException(this + " is a derived value");
  }

  @Override
  public String getName() {
    return "derived";
  }

}
