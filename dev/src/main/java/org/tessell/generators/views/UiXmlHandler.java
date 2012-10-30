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
  final Stack<String> parentUiFieldOperation = new Stack<String>();
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

    final int indexOfUiField = attributes.getIndex("urn:ui:com.google.gwt.uibinder", "field");
    boolean hasUpperCase = !localName.toLowerCase().equals(localName);

    if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("with")) {
      // ui:with
      final String type = attributes.getValue(attributes.getIndex("type"));
      final String name = attributes.getValue(attributes.getIndex("field"));
      withFields.add(new UiWithDeclaration(type, name));
    } else if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("style")) {
      // ui:style
      int fieldIndex = attributes.getIndex("field");
      int typeIndex = attributes.getIndex("type");
      if (typeIndex > -1) {
        final String name = fieldIndex == -1 ? "style" : attributes.getValue(fieldIndex);
        final String type = attributes.getValue(typeIndex);
        lastStyle = new UiStyleDeclaration(type, name);
        styleFields.add(lastStyle);
      }
    } else if (uri.equals("")) {
      // elements, only pick up elements with ui:fields
      if (indexOfUiField > -1) {
        final String name = attributes.getValue(indexOfUiField);
        uiFields.add(new UiFieldDeclaration(Element.class.getName(), name, null));
      }
    } else if (!uri.equals("") && !uri.equals("urn:ui:com.google.gwt.uibinder") && !hasUpperCase) {
      // probably (hopefully) a UiChild. Unfortunately, this is wild, random guess because
      // we a) we have to reinvent UiBinder's lookup strategy, and b) even worse, because Tessell's
      // codegen runs at buildtime, we can't rely on .class files or reflection being available for
      // every single type mentioned in a ui.xml file. So, instead we just hope the convention holds.
      String operation = "add" + localName.substring(0, 1).toUpperCase() + localName.substring(1);
      parentUiFieldOperation.push(getParentOperationOrNull().split("\\.")[0] + "." + operation);
      doParentPop = true;
    } else if (!uri.equals("") && !uri.equals("urn:ui:com.google.gwt.uibinder") && hasUpperCase) {
      // all other widgets
      final String name;
      if (indexOfUiField == -1) {
        name = "_anonymous" + (anonymousIndex++);
      } else {
        name = attributes.getValue(indexOfUiField);
      }
      final String type = (StringUtils.substringAfterLast(uri, ":") + "." + localName) //
        .replace("com.google.gwt.user.client.ui", "org.tessell.gwt.user.client.ui"); // use the subclasses
      uiFields.add(new UiFieldDeclaration(type, name, getParentOperationOrNull()));
      parentUiFieldOperation.push(name + ".add");
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
      parentUiFieldOperation.pop();
    }
  }

  private String getParentOperationOrNull() {
    if (parentUiFieldOperation.isEmpty()) {
      return null;
    } else {
      return parentUiFieldOperation.peek();
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
