package org.tessell.generators.views;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

/** A DTO for {@code ui:with} or {@code ui:field} declarations. */
class UiFieldDeclaration implements Comparable<UiFieldDeclaration> {

  final String type;
  final String name;
  final boolean isElement;
  // only available if the file has changed
  final Map<String, String> attributes;

  UiFieldDeclaration(final String type, final String name, final Attributes attributes) {
    this.type = type;
    this.name = name;
    if (attributes == null) {
      this.attributes = null;
    } else {
      // need to make a copy of attributes as SAX will mutate it
      this.attributes = new HashMap<String, String>();
      for (int i = 0; i < attributes.getLength(); i++) {
        this.attributes.put(attributes.getLocalName(i), attributes.getValue(i));
      }
    }
    isElement = type.contains("dom");
  }

  @Override
  public int compareTo(final UiFieldDeclaration o) {
    return type.compareTo(o.type);
  }
}
