package org.gwtmpv.widgets;

public interface IsInsertPanel extends IsIndexedPanel {

  void add(IsWidget widget);

  void insert(IsWidget widget, int beforeIndex);

}
