package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.cell.client.Cell;

public class StubCellList<T> extends StubWidget implements IsCellList<T> {

  @SuppressWarnings("unused")
  private final Cell<T> cell;

  public StubCellList(Cell<T> cell) {
    this.cell = cell;
  }

  @Override
  public void setRowData(int start, List<T> values) {
  }

}
