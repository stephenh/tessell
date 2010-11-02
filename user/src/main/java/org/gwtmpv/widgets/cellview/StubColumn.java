package org.gwtmpv.widgets.cellview;

import org.gwtmpv.widgets.StubCellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

public class StubColumn<T, C> implements IsColumn<T, C> {

  private StubCellTable<T> stubCellTable;

  public StubColumn(final ColumnValue<T, C> value, final Cell<C> cell) {
    // tell the stub about us
    ((StubCell<C>) cell).setStubCellValue(new StubCell.StubCellValue<C>() {
      @Override
      public C getValue(int displayedIndex) {
        T item = stubCellTable.getDisplayedItems().get(displayedIndex);
        return value.get(item);
      }

      @Override
      public void setValue(int displayedIndex, C newValue) {
        T item = stubCellTable.getDisplayedItems().get(displayedIndex);
        value.set(item, newValue);
      }
    });
  }

  public void setStubCellTable(StubCellTable<T> stubCellTable) {
    this.stubCellTable = stubCellTable;
  }

  @Override
  public Column<T, C> asColumn() {
    throw new IllegalStateException("This is a stub");
  }

}
