package org.tessell.model.dsl;

import com.google.gwt.user.client.TakesValue;

/**
 * A interface for settings values, basically like {@link TakesValue}
 * but without a {@code getValue} so that we can use this with lambdas.
 */
@FunctionalInterface
public interface SetsValue<T> {

  void setValue(T value);

}
