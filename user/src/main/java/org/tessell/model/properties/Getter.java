package org.tessell.model.properties;

@FunctionalInterface
public interface Getter<T> {
  T get();
}
