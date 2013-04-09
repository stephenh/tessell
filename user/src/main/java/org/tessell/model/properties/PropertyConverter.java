package org.tessell.model.properties;

/**
 * Logic to convert {@code A} to {@code B}, one-way.
 *
 * See {@link PropertyFormatter} for two-way.
 *
 * @param <A> the source type
 * @param <B> the destination type
 */
public interface PropertyConverter<A, B> {

  B to(A a);

}
