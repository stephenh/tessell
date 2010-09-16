package org.gwtmpv.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasText;

public interface IsElement extends HasCss, HasText {

  String getId();

  void setId(String id);

  int getOffsetWidth();

  int getOffsetHeight();

  String getAttribute(String name);

  void setAttribute(String name, String value);

  String getInnerText();

  void setInnerText(String text);

  String getInnerHTML();

  void setInnerHTML(String html);

  void ensureDebugId(String id);

  Element asElement();

}
