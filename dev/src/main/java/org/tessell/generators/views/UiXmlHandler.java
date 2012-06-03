package org.tessell.generators.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.dom.client.Element;

/** A SAX handler for ui.xml files. */
class UiXmlHandler extends DefaultHandler {

  final List<UiFieldDeclaration> withFields = new ArrayList<UiFieldDeclaration>();
  final List<UiFieldDeclaration> uiFields = new ArrayList<UiFieldDeclaration>();
  final List<UiStyleDeclaration> styleFields = new ArrayList<UiStyleDeclaration>();
  String firstTagType;
  private UiStyleDeclaration lastStyle;

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
    if (firstTagType == null && uri.startsWith("urn:import")) {
      firstTagType = StringUtils.substringAfterLast(uri, ":") + "." + localName;
    }

    // ui:with
    if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("with")) {
      final String type = attributes.getValue(attributes.getIndex("type"));
      final String name = attributes.getValue(attributes.getIndex("field"));
      withFields.add(new UiFieldDeclaration(type, name, attributes));
    }

    // ui:style
    if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("style")) {
      int fieldIndex = attributes.getIndex("field");
      int typeIndex = attributes.getIndex("type");
      if (typeIndex > -1) {
        final String name = fieldIndex == -1 ? "style" : attributes.getValue(fieldIndex);
        final String type = attributes.getValue(typeIndex);
        lastStyle = new UiStyleDeclaration(type, name);
        styleFields.add(lastStyle);
      }
    }

    // ui:field
    final int indexOfUiField = attributes.getIndex("urn:ui:com.google.gwt.uibinder", "field");
    if (indexOfUiField > -1) {
      final String type;
      if (uri.equals("")) {
        type = Element.class.getName();
      } else {
        type = (StringUtils.substringAfterLast(uri, ":") + "." + localName)
          .replace("com.google.gwt.user.client.ui", "org.tessell.gwt.user.client.ui"); // use the subclasses
      }
      final String name = attributes.getValue(indexOfUiField);
      uiFields.add(new UiFieldDeclaration(type, name, attributes));
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (lastStyle != null) {
      lastStyle.css += new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String namespaceURI, String localName, String qName) {
    lastStyle = null;
  }
}
