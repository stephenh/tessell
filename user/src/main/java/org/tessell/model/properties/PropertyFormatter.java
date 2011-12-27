package org.tessell.model.properties;

/**
 * Logic to convert {@code A} to {@code B}.
 *
 * @param <A> the source type
 * @param <B> the destination type
 */
public interface PropertyFormatter<A, B> {
  B format(A a);

  A parse(B b) throws Exception;
}
