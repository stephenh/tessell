package org.tessell.widgets.cellview;


public interface IsCellTable<T> extends IsAbstractCellTable<T> {

  void setTableLayoutFixed(boolean isFixed);

  void setWidth(String width, boolean isFixedLayout);

}
