package org.gwtmpv.widgets;

public class StubDataGrid<T> extends StubAbstractCellTable<T> implements IsDataGrid<T> {

  public StubDataGrid() {
  }

  public StubDataGrid(int pageSize) {
    super(pageSize);
  }

}
