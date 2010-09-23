package org.gwtmpv.widgets;

public interface IsIndexedPanel {

  IsWidget getIsWidget(int index);

  int getWidgetCount();

  int getWidgetIndex(IsWidget child);

  boolean remove(int index);

}
