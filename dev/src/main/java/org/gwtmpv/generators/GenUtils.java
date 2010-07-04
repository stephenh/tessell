package org.gwtmpv.generators;

import java.util.HashMap;
import java.util.Map;

import joist.util.Inflector;

public class GenUtils {

  /** Hacky way of doing command args -> map. */
  public static Map<String, String> parseArgs(final String[] args) {
    final Map<String, String> settings = new HashMap<String, String>();
    for (int i = 0; i < args.length;) {
      settings.put(args[i++].substring(2), args[i++]);
    }
    return settings;
  }

  /** Munge a CSS class name into a Java identifier. */
  public static String toMethodName(final String className) {
    final StringBuilder sb = new StringBuilder();
    char c = className.charAt(0);
    boolean nextUpCase = false;

    if (Character.isJavaIdentifierStart(c)) {
      sb.append(Character.toLowerCase(c));
    }

    for (int i = 1, j = className.length(); i < j; i++) {
      c = className.charAt(i);
      if (!Character.isJavaIdentifierPart(c)) {
        nextUpCase = true;
        continue;
      }

      if (nextUpCase) {
        nextUpCase = false;
        c = Character.toUpperCase(c);
      }
      sb.append(c);
    }

    // throw in a camelize for good measure
    return Inflector.uncapitalize(Inflector.camelize(sb.toString()));
  }

}
