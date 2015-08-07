package org.tessell.model.properties;

/**
 * Logic to convert {@code A} to {@code B}, one-way.
 *
 * See {@link PropertyFormatter} for two-way.
 *
 * @param <A> the source type
 * @param <B> the destination type
 */
@FunctionalInterface
public interface PropertyConverter<A, B> {

  abstract B to(A a);

  default B nullValue() {
    return null;
  }

}
