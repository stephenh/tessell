package org.tessell.generators.views;

import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;
import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;
import static org.apache.commons.lang.StringUtils.substringBetween;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.StubElement;

import com.google.gwt.dom.client.Element;

/** Holds mappings of ui.xml type -> interface type, stub type, and subclass type. */
public class Config {

  private static String[] viewgenFiles = new String[] { "viewgen.properties", "viewgen-root.properties" };
  private final Map<String, String> typeToInterface = new HashMap<>();
  private final Map<String, String> typeToStub = new HashMap<>();
  private final Map<String, List<String>> stubToCstrParams = new HashMap<>();
  private final String[] additionalViewgenFiles;

  public Config(String... additionalViewgenFiles) {
    this.additionalViewgenFiles = additionalViewgenFiles;
    loadViewGenDotProperties();
  }

  public String getInterface(final String type) {
    if (Element.class.getName().equals(type)) {
      return IsElement.class.getName();
    }
    if (typeToInterface.get(type) != null) {
      return typeToInterface.get(type);
    }
    throw new RuntimeException("No interface for " + type);
  }

  public String getStub(final String type) {
    if (Element.class.getName().equals(type)) {
      return StubElement.class.getName();
    }
    if (typeToStub.get(type) != null) {
      return typeToStub.get(type);
    }
    throw new RuntimeException("No stub for " + type);
  }

  public List<String> getStubCstrParams(final String type) {
    List<String> t = stubToCstrParams.get(type);
    if (t == null) {
      return Collections.emptyList();
    }
    return t;
  }

  public String getViewgenTimestamp() {
    return Integer.toString(typeToInterface.hashCode() + typeToStub.hashCode() + stubToCstrParams.hashCode());
  }

  private void loadViewGenDotProperties() {
    try {
      for (String file : viewgenFiles) {
        Enumeration<URL> urls = Config.class.getClassLoader().getResources(file);
        while (urls.hasMoreElements()) {
          URL url = urls.nextElement();
          // System.out.println("Loading " + url);
          try (InputStream in = url.openStream()) {
            loadViewGenFile(in);
          }
        }
      }
      for (String file : additionalViewgenFiles) {
        try (InputStream in = new FileInputStream(file)) {
          loadViewGenFile(in);
        }
      }
    } catch (IOException io) {
      throw new RuntimeException(io);
    }
  }

  private void loadViewGenFile(InputStream in) throws IOException {
    final Properties p = new Properties();
    p.load(in);
    for (final Entry<Object, Object> e : p.entrySet()) {
      final String type = e.getKey().toString();
      final String packageName = substringBeforeLast(type, ".");

      String line = e.getValue().toString();
      typeToInterface.put(type, prependPackageIfNeeded(packageName, substringBefore(line, ",")));

      // watch for stub with cstr params, e.g. TextLine(AppRegistry)
      String stub = substringAfter(line, ",");
      String[] params = new String[] {};
      if (stub.contains("(")) {
        params = splitPreserveAllTokens(substringBetween(stub, "(", ")"), ",");
        stub = substringBefore(stub, "(");
      }
      stub = prependPackageIfNeeded(packageName, stub);
      typeToStub.put(type, stub);
      stubToCstrParams.put(stub, Arrays.asList(params));
    }
  }

  /** Prepends {@code packageName} if the class name {@name} has no existing package. */
  private static String prependPackageIfNeeded(final String packageName, final String name) {
    if (name.contains(".")) {
      return name;
    } else {
      return packageName + "." + name;
    }
  }

}
