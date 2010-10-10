package org.gwtmpv.generators.views;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.split;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/** Caches the "ui:with" types of ui.xml files so we don't have to re-parse each file every time. */
public class ViewGeneratorCache {

  private final File cache = new File("./.viewGenerator.cache");
  private final Map<String, Entry> entries = new HashMap<String, Entry>();

  public ViewGeneratorCache() {
  }

  /** @return whether we have an entry for {@code uiXmlFile} */
  public boolean has(UiXmlFile uiXml) {
    return entries.keySet().contains(uiXml.getPath());
  }

  /** Updates the {@code uiXmlFile} entry with {@code withTypes}. */
  public void update(UiXmlFile uiXml) {
    entries.put(uiXml.getPath(), new Entry(uiXml.getPath(), uiXml.getWithTypes()));
  }

  public List<UiFieldDeclaration> getWithTypes(UiXmlFile uiXml) {
    return entries.get(uiXml.getPath()).getWithTypes();
  }

  /** Saves the cache to the file system for loading next time. */
  public void save() {
    try {
      FileUtils.writeLines(cache, entriesToLines());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void loadIfExists() {
    if (cache.exists()) {
      try {
        for (Object line : FileUtils.readLines(cache)) {
          Entry e = new Entry((String) line);
          entries.put(e.uiXml(), e);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private List<String> entriesToLines() {
    List<String> lines = new ArrayList<String>();
    for (Entry e : entries.values()) {
      lines.add(e.toLine());
    }
    return lines;
  }

  /** One entry per ui.xml file. */
  private class Entry {
    private final String[] parts;

    private Entry(String line) {
      parts = split(line, ",");
    }

    private Entry(String uiXmlFile, List<UiFieldDeclaration> withTypes) {
      parts = new String[withTypes.size() + 1];
      parts[0] = uiXmlFile;
      int i = 1;
      for (UiFieldDeclaration withType : withTypes) {
        parts[i++] = withType.type + " " + withType.name;
      }
    }

    private List<UiFieldDeclaration> getWithTypes() {
      List<UiFieldDeclaration> withTypes = new ArrayList<UiFieldDeclaration>();
      for (int i = 1; i < parts.length; i++) {
        String[] p = parts[i].split(" ");
        withTypes.add(new UiFieldDeclaration(p[0], p[1]));
      }
      return withTypes;
    }

    private String uiXml() {
      return parts[0];
    }

    private String toLine() {
      return join(parts, ",");
    }
  }

}
