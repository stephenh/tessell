package org.tessell.widgets;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class TextList extends Widget implements IsTextList {

  private String childTag = "dd";
  private String childStyleName = null;
  private boolean enabled = true;

  public TextList() {
    setElement(Document.get().createDivElement());
  }

  @Override
  public void add(final String text) {
    final Element child = DOM.createElement(childTag);
    child.setInnerText(text);
    if (childStyleName != null) {
      child.addClassName(childStyleName);
    }
    getElement().appendChild(child);
  }

  @Override
  public void remove(final String text) {
    final NodeList<Node> nodes = getElement().getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      final Node node = nodes.getItem(i);
      if (Element.is(node)) {
        final Element element = Element.as(node);
        if (element.getInnerText().equals(text)) {
          getElement().removeChild(node);
          break;
        }
      }
    }
  }

  @Override
  public void clear() {
    final NodeList<Node> nodes = getElement().getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      final Node node = nodes.getItem(i);
      if (Element.is(node)) {
        getElement().removeChild(node);
      }
    }
  }

  public String getChildTag() {
    return childTag;
  }

  public void setChildTag(final String childTag) {
    this.childTag = childTag;
  }

  public String getChildStyleName() {
    return childStyleName;
  }

  public void setChildStyleName(final String childStyleName) {
    this.childStyleName = childStyleName;
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
  }

  @Override
  public boolean hasErrors() {
    return getElement().getChildCount() > 0;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

}
