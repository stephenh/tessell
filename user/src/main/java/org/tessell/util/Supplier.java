package org.tessell.util;

/** Generic supplier, replace if guava ever becomes a dependency. */
public interface Supplier<T> {

  T get();

}
