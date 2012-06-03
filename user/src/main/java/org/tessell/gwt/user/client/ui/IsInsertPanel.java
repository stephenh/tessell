package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;

public interface IsInsertPanel extends IsIndexedPanel {

  void add(IsWidget widget);

  void insert(IsWidget widget, int beforeIndex);

}
