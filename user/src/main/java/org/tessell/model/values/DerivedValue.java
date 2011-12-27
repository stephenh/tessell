package org.tessell.model.values;

import org.tessell.model.properties.Property;

/**
 * Wraps a function as a {@link Value}.
 * 
 * This is for making {@link Property}s around a value you compute with a function.
 */
public abstract class DerivedValue<P> implements Value<P> {

  private final String name;

  public DerivedValue() {
    this.name = "derived";
  }

  public DerivedValue(String name) {
    this.name = name;
  }

  @Override
  public abstract P get();

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  public void set(final P value) {
    throw new IllegalStateException(this + " is a derived value");
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + " " + get();
  }

}
