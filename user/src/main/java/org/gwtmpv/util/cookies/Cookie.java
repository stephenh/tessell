package org.gwtmpv.util.cookies;

/** Abstracts out getting/settings values out of a cookie. */
public interface Cookie<T> {

  T get();

  void set(T value);

  void unset();

}
