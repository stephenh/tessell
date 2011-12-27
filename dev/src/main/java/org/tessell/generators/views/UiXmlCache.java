package org.tessell.generators.views;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joist.util.Join;

import org.apache.commons.io.FileUtils;

/** Caches the "ui:with" types of {@code ui.xml} files between {@code ViewGenerator} runs so that we don't have to re-parse each file every time. */
public class UiXmlCache {

  private static final int cacheVersion = 1;
  private final Map<String, Entry> entries = new HashMap<String, Entry>();
  private final File cache;

  public UiXmlCache(final File outputDirectory) {
    cache = new File(outputDirectory, "./.viewGenerator." + cacheVersion + ".cache");
  }

  /** @return whether we have an entry for {@code uiXmlFile} */
  public boolean has(UiXmlFile uiXml) {
    return entries.keySet().contains(uiXml.getPath());
  }

  /** Updates the {@code uiXmlFile} entry with {@code withTypes}. */
  public void update(UiXmlFile uiXml) {
    entries.put(uiXml.getPath(), new Entry(uiXml.getPath(), uiXml.getWiths(), uiXml.getStyles()));
  }

  /** @return the {@code ui:with} declarations for {@code uiXml}. */
  public List<UiFieldDeclaration> getCachedWiths(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).withs;
  }

  /** @return the {@code ui:style} declarations for {@code uiXml}. */
  public List<UiStyleDeclaration> getCachedStyles(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).styles;
  }

  /** Saves the cache to the file system for loading next time. */
  public void save() {
    try {
      FileUtils.writeLines(cache, entriesToLines());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Populates our cache from the last run's output, if available. */
  public void loadIfExists() {
    if (cache.exists()) {
      try {
        for (Object line : FileUtils.readLines(cache)) {
          Entry e = new Entry((String) line);
          entries.put(e.uiXmlFileName, e);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
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
  private class Entry {
    // each entry is serialized to 1 line in the cache file, comma separated into a String[]
    private final String uiXmlFileName;
    private final ArrayList<UiFieldDeclaration> withs = new ArrayList<UiFieldDeclaration>();
    private final ArrayList<UiStyleDeclaration> styles = new ArrayList<UiStyleDeclaration>();

    /** Make a new entry from the cached line. */
    private Entry(String line) {
      final String[] parts = splitPreserveAllTokens(line, ";");
      uiXmlFileName = parts[0];
      if (parts[1].length() > 0) {
        for (String with : parts[1].split(",")) {
          String[] wp = with.split(" ");
          withs.add(new UiFieldDeclaration(wp[0], wp[1], null));
        }
      }
      if (parts[2].length() > 0) {
        for (String style : parts[2].split(",")) {
          String[] sp = style.split(" ");
          styles.add(new UiStyleDeclaration(sp[0], sp[1]));
        }
      }
    }

    /** Make a new entry from a new {@code ui.xml} file with its {@code ui:with} types. */
    private Entry(String uiXmlFileName, List<UiFieldDeclaration> withs, List<UiStyleDeclaration> styles) {
      this.uiXmlFileName = uiXmlFileName;
      this.withs.addAll(withs);
      this.styles.addAll(styles);
    }

    private String toLine() {
      final List<String> withStrings = new ArrayList<String>();
      for (final UiFieldDeclaration with : withs) {
        withStrings.add(with.type + " " + with.name);
      }
      final List<String> styleStrings = new ArrayList<String>();
      for (final UiStyleDeclaration style : styles) {
        styleStrings.add(style.type + " " + style.name);
      }
      final String p0 = uiXmlFileName;
      final String p1 = Join.comma(withStrings);
      final String p2 = Join.comma(styleStrings);
      return join(new String[] { p0, p1, p2 }, ";");
    }
  }

}
