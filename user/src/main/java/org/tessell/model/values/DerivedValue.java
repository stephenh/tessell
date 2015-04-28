package org.tessell.model.values;

import org.tessell.model.properties.Property;

/**
 * Wraps a function, implemented by a subclasses in {@link #get()}, as a {@link Value}.
 *
 * This is for making {@link Property}s around a value you compute with a function.
 *
 * Note for using Java 8 lambdas, you can use {@link LambdaValue}.
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
  public final boolean isReadOnly() {
    return true;
  }

  @Override
  public final void set(final P value) {
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
