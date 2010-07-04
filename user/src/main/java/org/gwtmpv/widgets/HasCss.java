package org.gwtmpv.widgets;

// Could rename to HasStyleNames
public interface HasCss {

  void addStyleName(String styleName);

  void removeStyleName(String styleName);

  String getStyleName();

  IsStyle getStyle();

}
