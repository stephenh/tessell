package org.tessell.generators.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import joist.util.Reflection;

import org.apache.commons.lang.StringUtils;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.StubElement;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.dom.client.Element;

/** Holds mappings of ui.xml type -> interface type, stub type, and subclass type. */
public class Config {

  private final Map<String, String> typeToInterface = new HashMap<String, String>();
  private final Map<String, String> typeToStub = new HashMap<String, String>();

  public Config() {
    // if the user's project has both widgets and ui.xml files in the same codebase,
    // the widget .class files won't be around to access the OtherTypes annotation,
    // so fall back on our old viewgen.properties file
    setupUsersViewGenDotProperties();
  }

  public String getInterface(final String type) {
    Class<?> clazz = Reflection.forNameOrNull(type);
    if (clazz != null) {
      OtherTypes ot = clazz.getAnnotation(OtherTypes.class);
      if (ot != null) {
        return ot.intf().getName();
      }
    }
    if (Element.class.getName().equals(type)) {
      return IsElement.class.getName();
    }
    if (typeToInterface.get(type) != null) {
      return typeToInterface.get(type);
    }
    throw new RuntimeException("No interface for " + type);
  }

  public String getStub(final String type) {
    Class<?> clazz = Reflection.forNameOrNull(type);
    if (clazz != null) {
      OtherTypes ot = clazz.getAnnotation(OtherTypes.class);
      if (ot != null) {
        return ot.stub().getName();
      }
    }
    if (Element.class.getName().equals(type)) {
      return StubElement.class.getName();
    }
    if (typeToStub.get(type) != null) {
      return typeToStub.get(type);
    }
    throw new RuntimeException("No stub for " + type);
  }

  private void setupUsersViewGenDotProperties() {
    final InputStream in = ViewGenerator.class.getResourceAsStream("/viewgen.properties");
    if (in != null) {
      final Properties p = new Properties();
      try {
        p.load(in);
      } catch (final IOException io) {
        throw new RuntimeException(io);
      }
      for (final Entry<Object, Object> e : p.entrySet()) {
        final String type = e.getKey().toString();
        final String packageName = StringUtils.substringBeforeLast(type, ".");
        final String[] parts = e.getValue().toString().split(",");
        typeToInterface.put(type, prependPackageIfNeeded(packageName, parts[0].trim()));
        typeToStub.put(type, prependPackageIfNeeded(packageName, parts[1].trim()));
      }
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
