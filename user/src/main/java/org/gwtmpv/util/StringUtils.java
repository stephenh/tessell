package org.gwtmpv.util;

import java.util.ArrayList;
import java.util.Iterator;

public class StringUtils {

  public static String string(Object o) {
    return o == null ? "" : o.toString();
  }

  public static String defaultString(final String a, final String defaultString) {
    return a != null ? a : defaultString;
  }

  public static String emptyString(final String a, final String defaultString) {
    return a != null && a.trim().length() > 0 ? a : defaultString;
  }

  public static String join(final ArrayList<String> things, final String on) {
    final StringBuilder sb = new StringBuilder();
    for (final Iterator<String> i = things.iterator(); i.hasNext();) {
      sb.append(i.next());
      if (i.hasNext()) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  public static String join(final String[] things, final String on) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0, len = things.length; i < len; i++) {
      sb.append(things[i]);
      if (i < len - 1) {
        sb.append(" ");
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

}
