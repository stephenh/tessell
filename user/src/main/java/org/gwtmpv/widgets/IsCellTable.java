package org.gwtmpv.widgets;

public interface IsCellTable<T> extends IsAbstractCellTable<T> {

  void setTableLayoutFixed(boolean isFixed);

  void setWidth(String width, boolean isFixedLayout);

}
