package org.tessell.generators.views;

import joist.util.Reflection;

import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.StubElement;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.dom.client.Element;

/** Holds mappings of ui.xml type -> interface type, stub type, and subclass type. */
public class Config {

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
    throw new RuntimeException("No stub for " + type);
  }

}
