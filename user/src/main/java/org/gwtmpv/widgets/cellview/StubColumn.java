package org.gwtmpv.widgets.cellview;

import org.gwtmpv.widgets.StubCellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

public class StubColumn<T, C> implements IsColumn<T, C>, StubCell.StubCellValue<C> {

  private final ColumnValue<T, C> columnValue;
  private final Cell<C> cell;
  private StubCellTable<T> stubCellTable;

  public StubColumn(final ColumnValue<T, C> columnValue, final Cell<C> cell) {
    this.columnValue = columnValue;
    this.cell = cell;
    ((StubCell<C>) cell).setStubCellValue(this); // tell the stub about us
  }

  public void setStubCellTable(StubCellTable<T> stubCellTable) {
    this.stubCellTable = stubCellTable;
  }

  public Cell<C> getCell() {
    return cell;
  }

  @Override
  public Column<T, C> asColumn() {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public C getValue(int displayedIndex) {
    T item = stubCellTable.getDisplayedItems().get(displayedIndex);
    return columnValue.get(item);
  }

  @Override
  public void setValue(int displayedIndex, C value) {
    T item = stubCellTable.getDisplayedItems().get(displayedIndex);
    columnValue.set(item, value);
  }

  @Override
  public FieldUpdater<T, C> getFieldUpdater() {
    return new FieldUpdater<T, C>() {
      public void update(int index, T object, C value) {
        columnValue.set(object, value);
      }
    };
  }

  @Override
  public C getValue(T object) {
    return columnValue.get(object);
  }

}
