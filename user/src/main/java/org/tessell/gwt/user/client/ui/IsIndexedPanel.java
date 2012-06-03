package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;

public interface IsIndexedPanel extends IsWidget {

  IsWidget getIsWidget(int index);

  int getWidgetCount();

  int getWidgetIndex(IsWidget child);

  boolean remove(int index);

}
