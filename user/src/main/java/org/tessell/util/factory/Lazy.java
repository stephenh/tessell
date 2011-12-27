package org.tessell.util.factory;

public class Lazy<T> {

  private final Factory<T> factory;
  private T value;
  private boolean gotten = false;

  public static <T> Lazy<T> of(final Factory<T> maker) {
    return new Lazy<T>(maker);
  }

  public Lazy(final Factory<T> maker) {
    factory = maker;
  }

  public T get() {
    if (!gotten) {
      value = factory.create();
      gotten = true;
    }
    return value;
  }

}
