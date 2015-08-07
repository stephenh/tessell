package org.tessell.util;

/** Generic supplier, replace if guava ever becomes a dependency. */
@FunctionalInterface
public interface Supplier<T> {

  T get();

}
