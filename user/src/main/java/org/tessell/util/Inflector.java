package org.tessell.util;

import static org.tessell.util.StringUtils.capitalize;
import static org.tessell.util.StringUtils.uncapitalize;

import java.util.ArrayList;
import java.util.Iterator;

/** Copy/paste from joist.util. */
public class Inflector {

  private static final String lower = "[a-z]";
  private static final String upper = "[A-Z0-9]";

  private Inflector() {
  }

  public static String humanize(final String camelCased) {
    String name = "";
    for (final Iterator<String> i = split(camelCased).iterator(); i.hasNext();) {
      name += capitalize(i.next());
      if (i.hasNext()) {
        name += " ";
      }
    }
    return name;
  }

  public static String camelize(final String humanCased) {
    String name = "";
    for (final Iterator<String> i = split(humanCased).iterator(); i.hasNext();) {
      name += i.next();
    }
    return uncapitalize(name);
  }

  /** Splits word into groups based on where underscores and case changes are. */
  private static ArrayList<String> split(final String word) {
    final ArrayList<String> parts = new ArrayList<String>();
    boolean wasLower = false;
    int at = 0;
    final int length = word.length();
    for (int i = 0; i < length; i++) {
      final String c = word.substring(i, i + 1);
      if ("_".equals(c) || " ".equals(c)) {
        parts.add(word.substring(at, i));
        at = i + 1;
      } else if (wasLower && c.matches(upper)) {
        parts.add(word.substring(at, i));
        at = i;
        wasLower = false;
      } else if (!wasLower && c.matches(lower)) {
        wasLower = true;
      }
    }
    if (at != length) {
      parts.add(word.substring(at));
    }
    return parts;
  }

}
