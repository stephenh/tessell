package org.tessell.widgets;

public interface IsIndexedPanel extends IsWidget {

  IsWidget getIsWidget(int index);

  int getWidgetCount();

  int getWidgetIndex(IsWidget child);

  boolean remove(int index);

}
