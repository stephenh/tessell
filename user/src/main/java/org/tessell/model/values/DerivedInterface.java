package org.tessell.model.values;

import org.tessell.model.properties.Property;

/**
 * Wraps a function as a {@link Value}.
 *
 * This is for making {@link Property}s around a value you compute with a function.
 */
public interface DerivedInterface<P> extends Value<P> {

  // This should be the only abstract method
  @Override
  abstract P get();

  @Override
  default boolean isReadOnly() {
    return true;
  }

  @Override
  default void set(final P value) {
    throw new IllegalStateException(this + " is a derived value");
  }

  @Override
  default String getName() {
    return "derived";
  }
}
