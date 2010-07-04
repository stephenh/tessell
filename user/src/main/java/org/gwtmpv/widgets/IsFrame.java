package org.gwtmpv.widgets;

public interface IsFrame extends IsWidget {

  String getUrl();

  void setUrl(String url);

  // Should be in UIObject
  void setSize(String width, String height);

}
