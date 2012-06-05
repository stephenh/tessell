package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.IsStyle;

// Could rename to HasStyleNames
public interface HasCss {

  void addStyleName(String styleName);

  void removeStyleName(String styleName);

  void setStyleName(String styleName);

  String getStyleName();

  IsStyle getStyle();

}
