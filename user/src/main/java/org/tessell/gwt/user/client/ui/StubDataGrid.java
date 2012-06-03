package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.cellview.StubAbstractCellTable;

public class StubDataGrid<T> extends StubAbstractCellTable<T> implements IsDataGrid<T> {

  public StubDataGrid() {
  }

  public StubDataGrid(int pageSize) {
    super(pageSize);
  }

}
