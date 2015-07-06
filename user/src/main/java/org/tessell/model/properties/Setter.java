package org.tessell.model.properties;

@FunctionalInterface
public interface Setter<T> {
  void set(T value);
}
