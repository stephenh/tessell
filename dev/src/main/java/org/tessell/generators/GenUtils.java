package org.tessell.generators;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import joist.sourcegen.GClass;
import joist.util.Inflector;

import org.apache.commons.io.FileUtils;

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

  /** Saves {@code gc} to {@code outputDirectory} if needed. */
  public static void saveIfChanged(File outputDirectory, GClass gc) {
    File outputFile = new File(outputDirectory, gc.getFileName());
    saveIfChanged(outputFile, gc.toCode());
  }

  /** Saves {@code newContent} to {@code outputFile} if needed. */
  public static void saveIfChanged(File outputFile, String newContent) {
    try {
      String oldContent = outputFile.exists() ? FileUtils.readFileToString(outputFile) : "";
      if (!oldContent.equals(newContent)) {
        System.out.println(outputFile);
        FileUtils.writeStringToFile(outputFile, newContent);
      }
    } catch (IOException io) {
      throw new RuntimeException(io);
    }
  }

}
