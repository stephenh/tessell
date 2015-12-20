package org.tessell.gwt.dom.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
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
  public void setStyleName(String styleName) {
    element.setClassName(styleName);
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
  public int getOffsetTop() {
    return element.getOffsetTop();
  }

  @Override
  public int getOffsetLeft() {
    return element.getOffsetLeft();
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
  public int getClientHeight() {
    return element.getClientHeight();
  }

  @Override
  public int getClientWidth() {
    return element.getClientWidth();
  }

  @Override
  public int getScrollHeight() {
    return element.getScrollHeight();
  }

  @Override
  public int getScrollWidth() {
    return element.getScrollWidth();
  }

  @Override
  public int getScrollTop() {
    return element.getScrollTop();
  }

  @Override
  public int getScrollLeft() {
    return element.getScrollLeft();
  }

  @Override
  public void setScrollTop(int scrollTop) {
    element.setScrollTop(scrollTop);
  }

  @Override
  public void setScrollLeft(int scrollLeft) {
    element.setScrollLeft(scrollLeft);
  }

  @Override
  public void removeFromParent() {
    element.removeFromParent();
  }

  @Override
  public void setInnerSafeHtml(SafeHtml html) {
    element.setInnerHTML(html.asString());
  }

  @Override
  public void scrollIntoView() {
    element.scrollIntoView();
  }

  @Override
  public void insertFirst(IsElement element) {
    this.element.insertFirst(element.asElement());
  }

  @Override
  public void removeChild(IsElement element) {
    this.element.removeChild(element.asElement());
  }

  @Override
  public void removeAllChildren() {
    element.removeAllChildren();
  }

  @Override
  public int getChildCount() {
    return element.getChildCount();
  }

  @Override
  public IsElement getParentElement() {
    if (element.getParentElement() == null) {
      return null;
    } else {
      return new GwtElement(element.getParentElement());
    }
  }

}
