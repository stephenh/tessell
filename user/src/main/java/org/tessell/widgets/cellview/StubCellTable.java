package org.tessell.widgets.cellview;


public class StubCellTable<T> extends StubAbstractCellTable<T> implements IsCellTable<T> {

  public StubCellTable() {
  }

  public StubCellTable(int pageSize) {
    super(pageSize);
  }

  @Override
  public void setWidth(String width, boolean isFixedLayout) {
  }

  @Override
  public void setTableLayoutFixed(boolean isFixed) {
  }

}
