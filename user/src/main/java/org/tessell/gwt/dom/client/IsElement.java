package org.tessell.gwt.dom.client;

import org.tessell.gwt.user.client.ui.HasCss;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasText;

public interface IsElement extends HasCss, HasText {

  String getId();

  void setId(String id);

  int getOffsetWidth();

  int getOffsetHeight();

  int getOffsetTop();

  int getOffsetLeft();

  int getClientHeight();

  int getClientWidth();

  int getScrollHeight();

  int getScrollWidth();

  int getScrollTop();

  int getScrollLeft();

  void setScrollTop(int scrollTop);

  void setScrollLeft(int scrollLeft);

  String getAttribute(String name);

  void setAttribute(String name, String value);

  String getInnerText();

  void setInnerText(String text);

  String getInnerHTML();

  void setInnerHTML(String html);

  void setInnerSafeHtml(SafeHtml html);

  void ensureDebugId(String id);

  void insertFirst(IsElement element);

  void appendChild(IsElement element);

  void removeChild(IsElement element);

  void removeFromParent();

  void removeAllChildren();

  int getChildCount();

  Element asElement();

  IsElement getParentElement();

  void scrollIntoView();

}
