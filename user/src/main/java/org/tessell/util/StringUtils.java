package org.tessell.util;

import java.util.Iterator;
import java.util.List;

public class StringUtils {

  public static String string(Object o) {
    return o == null ? "" : o.toString();
  }

  public static String defaultString(final String a, final String defaultString) {
    return a != null ? a : defaultString;
  }

  public static String blankString(final String a, final String defaultString) {
    return a != null && a.trim().length() > 0 ? a : defaultString;
  }

  public static String emptyString(final String a, final String defaultString) {
    return a != null && a.length() > 0 ? a : defaultString;
  }

  public static String join(final List<?> things, final String on) {
    final StringBuilder sb = new StringBuilder();
    for (final Iterator<?> i = things.iterator(); i.hasNext();) {
      sb.append(i.next());
      if (i.hasNext()) {
        sb.append(on);
      }
    }
    return sb.toString();
  }

  public static String join(final Object[] things, final String on) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0, len = things.length; i < len; i++) {
      sb.append(things[i]);
      if (i < len - 1) {
        sb.append(on);
      }
    }
    return sb.toString();
  }

  public static String substringAfterLast(final String str, final String separator) {
    if (str == null) {
      return null;
    }
    final int pos = str.lastIndexOf(separator);
    if (pos == -1 || pos == (str.length() - separator.length())) {
      return "";
    }
    return str.substring(pos + separator.length());
  }

  public static String capitalize(final String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  public static String uncapitalize(final String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

}
