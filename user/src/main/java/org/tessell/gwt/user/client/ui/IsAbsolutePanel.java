package org.tessell.gwt.user.client.ui;

public interface IsAbsolutePanel extends IsComplexPanel {

  void add(com.google.gwt.user.client.ui.IsWidget w, int left, int top);

  int getIsWidgetLeft(com.google.gwt.user.client.ui.IsWidget w);

  int getIsWidgetTop(com.google.gwt.user.client.ui.IsWidget w);

  void insert(com.google.gwt.user.client.ui.IsWidget w, int beforeIndex);

  void insert(com.google.gwt.user.client.ui.IsWidget w, int left, int top, int beforeIndex);

  void setWidgetPosition(com.google.gwt.user.client.ui.IsWidget w, int left, int top);

}
