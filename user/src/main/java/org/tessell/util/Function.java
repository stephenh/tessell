package org.tessell.util;

/** Generic function, until it shows up in GWT. */
@FunctionalInterface
public interface Function<T, R> {

  R get(T param);

}
