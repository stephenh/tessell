package org.tessell.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasText;

public interface IsElement extends HasCss, HasText {

  String getId();

  void setId(String id);

  int getOffsetWidth();

  int getOffsetHeight();

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

  void ensureDebugId(String id);

  void appendChild(IsElement element);

  void appendChild(IsWidget widget);

  void removeFromParent();

  Element asElement();

}
