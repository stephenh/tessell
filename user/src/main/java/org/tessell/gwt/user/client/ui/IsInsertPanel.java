package org.tessell.gwt.user.client.ui;

import com.google.gwt.user.client.ui.IsWidget;

public interface IsInsertPanel extends IsIndexedPanel {

  void add(IsWidget widget);

  void insert(IsWidget widget, int beforeIndex);

}
