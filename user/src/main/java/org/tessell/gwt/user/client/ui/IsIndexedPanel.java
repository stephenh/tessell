package org.tessell.gwt.user.client.ui;

public interface IsIndexedPanel extends IsWidget {

  IsWidget getIsWidget(int index);

  int getWidgetCount();

  int getWidgetIndex(com.google.gwt.user.client.ui.IsWidget child);

  boolean remove(int index);

}
