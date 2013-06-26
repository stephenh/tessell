package org.tessell.model.properties;

/**
 * Logic to convert {@code A} to {@code B}, one-way.
 *
 * See {@link PropertyFormatter} for two-way.
 *
 * @param <A> the source type
 * @param <B> the destination type
 */
public abstract class PropertyConverter<A, B> {

  public abstract B to(A a);

  public B nullValue() {
    return null;
  }

}
