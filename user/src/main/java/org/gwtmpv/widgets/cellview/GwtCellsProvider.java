package org.gwtmpv.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;

public class GwtCellsProvider implements CellsProvider {

  @Override
  public IsTextCell newTextCell() {
    return new GwtTextCell();
  }

  @Override
  public IsClickableTextCell newClickableTextCell() {
    return new GwtClickableTextCell();
  }

  @Override
  public IsCheckboxCell newCheckboxCell() {
    return new GwtCheckboxCell();
  }

  @Override
  public <T, C> IsColumn<T, C> newColumn(ColumnValue<T, C> value, Cell<C> cell) {
    return new GwtColumn<T, C>(value, cell);
  }

  @Override
  public IsTextHeader newTextHeader(String text) {
    return new GwtTextHeader(text);
  }

  @Override
  public <C> IsHeader<C> newHeader(HeaderValue<C> value, Cell<C> cell) {
    return new GwtHeader<C>(value, cell);
  }

  @Override
  public <C> IsCompositeCell<C> newCompositeCell(List<HasCell<C, ?>> cells) {
    return new GwtCompositeCell<C>(cells);
  }

  @Override
  public IsHyperlinkCell newHyperlinkCell() {
    return new GwtHyperlinkCell();
  }

}
