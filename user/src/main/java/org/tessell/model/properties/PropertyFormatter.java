package org.tessell.model.properties;

/**
 * Logic to convert {@code A} to {@code B}, two-way.
 *
 * See {@link PropertyConverter} for one-way.
 *
 * @param <A> the source type
 * @param <B> the destination type
 */
public abstract class PropertyFormatter<A, B> {
  public abstract B format(A a);

  public abstract A parse(B b) throws Exception;

  public B nullValue() {
    return null;
  }
}
