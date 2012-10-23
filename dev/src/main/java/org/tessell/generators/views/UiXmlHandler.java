package org.tessell.generators.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.dom.client.Element;

/** A SAX handler for ui.xml files. */
class UiXmlHandler extends DefaultHandler {

  final List<UiWithDeclaration> withFields = new ArrayList<UiWithDeclaration>();
  // ui:field elements + ui:field widgets + anonymous widgets
  final List<UiFieldDeclaration> uiFields = new ArrayList<UiFieldDeclaration>();
  final List<UiStyleDeclaration> styleFields = new ArrayList<UiStyleDeclaration>();
  final Stack<String> parentUiFieldName = new Stack<String>();
  final Stack<Boolean> popParentUiFieldName = new Stack<Boolean>();
  String firstTagType;
  private UiStyleDeclaration lastStyle;
  private int anonymousIndex;

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
    boolean doParentPop = false;

    if (firstTagType == null && uri.startsWith("urn:import")) {
      firstTagType = StringUtils.substringAfterLast(uri, ":") + "." + localName;
    }

    // ui:with
    if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("with")) {
      final String type = attributes.getValue(attributes.getIndex("type"));
      final String name = attributes.getValue(attributes.getIndex("field"));
      withFields.add(new UiWithDeclaration(type, name));
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

    final int indexOfUiField = attributes.getIndex("urn:ui:com.google.gwt.uibinder", "field");
    // elements
    if (uri.equals("")) {
      // only pick up elements with ui:fields
      if (indexOfUiField > -1) {
        final String name = attributes.getValue(indexOfUiField);
        uiFields.add(new UiFieldDeclaration(Element.class.getName(), name, null));
      }
    }

    // all other widgets
    boolean hasUpperCase = !localName.toLowerCase().equals(localName);
    if (!uri.equals("") && !uri.equals("urn:ui:com.google.gwt.uibinder") && hasUpperCase) {
      final boolean anonymous = indexOfUiField == -1;
      final String name;
      if (anonymous) {
        name = "_anonymous" + (anonymousIndex++);
      } else {
        name = attributes.getValue(indexOfUiField);
      }
      final String type = (StringUtils.substringAfterLast(uri, ":") + "." + localName) //
        .replace("com.google.gwt.user.client.ui", "org.tessell.gwt.user.client.ui"); // use the subclasses
      uiFields.add(new UiFieldDeclaration(type, name, getParentNameOrNull()));
      parentUiFieldName.push(name);
      doParentPop = true;
    }

    popParentUiFieldName.push(doParentPop);
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
    boolean doParentPop = popParentUiFieldName.pop();
    if (doParentPop) {
      parentUiFieldName.pop();
    }
  }

  private String getParentNameOrNull() {
    if (parentUiFieldName.isEmpty()) {
      return null;
    } else {
      return parentUiFieldName.peek();
    }
  }

  List<UiFieldDeclaration> uiFieldWidgets() {
    List<UiFieldDeclaration> widgets = new ArrayList<UiFieldDeclaration>();
    for (UiFieldDeclaration uiField : uiFields) {
      if (!uiField.type.equals(Element.class.getName())) {
        widgets.add(uiField);
      }
    }
    return widgets;
  }
}
