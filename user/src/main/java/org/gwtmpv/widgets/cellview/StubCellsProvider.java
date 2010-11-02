package org.gwtmpv.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;

public class StubCellsProvider implements CellsProvider {

  public static void install() {
    Cells.setProvider(new StubCellsProvider());
  }

  @Override
  public IsTextCell newTextCell() {
    return new StubTextCell();
  }

  @Override
  public IsClickableTextCell newClickableTextCell() {
    return new GwtClickableTextCell();
  }

  @Override
  public IsCheckboxCell newCheckboxCell() {
    return new StubCheckboxCell();
  }

  @Override
  public <T, C> IsColumn<T, C> newColumn(ColumnValue<T, C> value, Cell<C> cell) {
    return new StubColumn<T, C>(value, cell);
  }

  @Override
  public IsTextHeader newTextHeader(String text) {
    return new StubTextHeader(text);
  }

  @Override
  public <C> IsHeader<C> newHeader(HeaderValue<C> value, Cell<C> cell) {
    return new StubHeader<C>(value, cell);
  }

  @Override
  public <C> IsCompositeCell<C> newCompositeCell(List<HasCell<C, ?>> cells) {
    return new StubCompositeCell<C>(cells);
  }

}
