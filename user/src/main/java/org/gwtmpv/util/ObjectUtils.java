package org.gwtmpv.util;

/** Utility methods, with cute names for static import-ability. */
public class ObjectUtils {

  /** @return true if {@code one} and {@code two} are both {@code null} or {@code equals} */
  public static <T> boolean eq(final T one, final T two) {
    return (one == null) ? two == null : one.equals(two);
  }

  /** return {@code object.toString()} or {@code ifNull} if it is null */
  public static String toStr(final Object object, final String ifNull) {
    return object == null ? ifNull : object.toString();
  }

}
