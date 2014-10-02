package org.tessell.generators.views;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joist.util.Join;

import org.apache.commons.io.FileUtils;

/**
 * Caches the "ui:with" types of {@code ui.xml} files between {@code ViewGenerator}
 * runs so that we don't have to re-parse each file every time.
 */
public class UiXmlCache {

  private static final int cacheVersion = 10;
  private static final String viewgenTimestampKey = "viewgenTimestamp";
  private final Map<String, Entry> entries = new HashMap<String, Entry>();
  private String viewgenTimestamp;

  public static UiXmlCache loadOrCreate(final File outputDirectory) {
    UiXmlCache c = new UiXmlCache();
    if (cache(outputDirectory).exists()) {
      try {
        for (String line : FileUtils.readLines(cache(outputDirectory))) {
          if (line.startsWith(viewgenTimestampKey)) {
            c.viewgenTimestamp = line.split("=")[1];
          } else {
            Entry e = new Entry(line);
            c.entries.put(e.uiXmlFileName, e);
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return c;
  }

  private static File cache(File outputDirectory) {
    return new File(outputDirectory, "./.viewGenerator." + cacheVersion + ".cache");
  }

  private UiXmlCache() {
  }

  /** @return whether we have an entry for {@code uiXmlFile} */
  public boolean has(UiXmlFile uiXml) {
    return entries.keySet().contains(uiXml.getPath());
  }

  /** Updates the {@code uiXmlFile} entry with {@code withTypes}. */
  public void update(UiXmlFile uiXml) {
    entries.put(uiXml.getPath(), new Entry(//
      uiXml.getPath(),
      uiXml.getFreshWiths(),
      uiXml.getFreshStyles(),
      uiXml.getStubDependencies()));
  }

  /** @return the {@code ui:with} declarations for {@code uiXml}. */
  public List<UiWithDeclaration> getCachedWiths(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).withs;
  }

  /** @return the {@code ui:style} declarations for {@code uiXml}. */
  public List<UiStyleDeclaration> getCachedStyles(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).styles;
  }

  /** @return the stub dependencies for {@code uiXml}. */
  public List<String> getCachedStubDependencies(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).stubDependencies;
  }

  /** Saves the cache to the file system for loading next time. */
  public void save(File outputDirectory, String viewgenTimestamp) {
    try {
      List<String> lines = entriesToLines();
      lines.add(viewgenTimestampKey + "=" + viewgenTimestamp);
      FileUtils.writeLines(cache(outputDirectory), lines);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getViewgenTimestamp() {
    return viewgenTimestamp;
  }

  /** @return the cache entries as a list of lines. */
  private List<String> entriesToLines() {
    List<String> lines = new ArrayList<String>();
    for (Entry e : entries.values()) {
      lines.add(e.toLine());
    }
    return lines;
  }

  /** One entry per ui.xml file. */
  private static class Entry {
    // each entry is serialized to 1 line in the cache file, comma separated into a String[]
    private final String uiXmlFileName;
    private final ArrayList<UiWithDeclaration> withs = new ArrayList<UiWithDeclaration>();
    private final ArrayList<UiStyleDeclaration> styles = new ArrayList<UiStyleDeclaration>();
    private final ArrayList<String> stubDependencies = new ArrayList<String>();

    /** Make a new entry from the cached line. */
    private Entry(String line) {
      final String[] parts = splitPreserveAllTokens(line, ";");
      uiXmlFileName = parts[0];
      // parse out ui:withs
      if (parts[1].length() > 0) {
        for (String with : parts[1].split(",")) {
          String[] wp = with.split(" ");
          withs.add(new UiWithDeclaration(wp[0], wp[1]));
        }
      }
      // parse out ui:styles
      if (parts[2].length() > 0) {
        for (String style : parts[2].split(",")) {
          String[] sp = style.split(" ");
          styles.add(new UiStyleDeclaration(sp[0], sp[1]));
        }
      }
      // parse out stub dependencies
      if (parts[3].length() > 0) {
        stubDependencies.addAll(Arrays.asList(parts[3]));
      }
    }

    /** Make a new entry from a new {@code ui.xml} file with its {@code ui:with} types. */
    private Entry(String uiXmlFileName, List<UiWithDeclaration> withs, List<UiStyleDeclaration> styles, List<String> stubDependencies) {
      this.uiXmlFileName = uiXmlFileName;
      this.withs.addAll(withs);
      this.styles.addAll(styles);
      this.stubDependencies.addAll(stubDependencies);
    }

    private String toLine() {
      final List<String> withStrings = new ArrayList<String>();
      for (final UiWithDeclaration with : withs) {
        withStrings.add(with.type + " " + with.name);
      }
      final List<String> styleStrings = new ArrayList<String>();
      for (final UiStyleDeclaration style : styles) {
        styleStrings.add(style.type + " " + style.name);
      }
      final String p0 = uiXmlFileName;
      final String p1 = Join.comma(withStrings);
      final String p2 = Join.comma(styleStrings);
      final String p3 = Join.comma(stubDependencies);
      return join(new String[] { p0, p1, p2, p3 }, ";");
    }
  }

}
