package org.tessell.model.values;

import org.tessell.model.properties.Property;

/**
 * Wraps a Java 8 lambda as a {@link Value}.
 *
 * This is for making {@link Property}s around a value you compute with a lambda.
 *
 * Note that while Tessell previously had {@link DerivedValue} for pre-Java 8
 * "lambdas as anonymous inner classes", DerivedValue is not a FunctionalInterface;
 * hence introducing {@code LambdaValue}.
 */
@FunctionalInterface
public interface LambdaValue<P> extends Value<P> {

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
