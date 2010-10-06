package org.gwtmpv.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class GwtElement implements IsElement {

  private final Element element;

  public GwtElement(final Element element) {
    this.element = element;
  }

  @Override
  public String getInnerText() {
    return element.getInnerText();
  }

  @Override
  public void setInnerText(final String text) {
    element.setInnerText(text);
  }

  @Override
  public void addStyleName(final String styleName) {
    element.addClassName(styleName);
  }

  @Override
  public void removeStyleName(final String styleName) {
    element.removeClassName(styleName);
  }

  @Override
  public IsStyle getStyle() {
    return new ElementHasStyle(element);
  }

  @Override
  public String getInnerHTML() {
    return element.getInnerHTML();
  }

  @Override
  public void setInnerHTML(final String html) {
    element.setInnerHTML(html);
  }

  @Override
  public void ensureDebugId(final String id) {
    UIObject.ensureDebugId(element, id);
  }

  @Override
  public String getStyleName() {
    return element.getClassName();
  }

  @Override
  public String getText() {
    return getInnerText();
  }

  @Override
  public void setText(final String text) {
    setInnerText(text);
  }

  @Override
  public String getAttribute(final String name) {
    return element.getAttribute(name);
  }

  @Override
  public void setAttribute(final String name, final String value) {
    element.setAttribute(name, value);
  }

  @Override
  public int getOffsetHeight() {
    return element.getOffsetHeight();
  }

  @Override
  public int getOffsetWidth() {
    return element.getOffsetWidth();
  }

  @Override
  public String getId() {
    return element.getId();
  }

  @Override
  public void setId(final String id) {
    element.setId(id);
  }

  @Override
  public Element asElement() {
    return element;
  }

  @Override
  public void appendChild(IsElement element) {
    this.element.appendChild(element.asElement());
  }

  @Override
  public void appendChild(IsWidget widget) {
    element.appendChild(widget.asWidget().getElement());
  }

}
