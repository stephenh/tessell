package org.gwtmpv.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;

public interface CellsProvider {

  IsTextCell newTextCell();

  IsClickableTextCell newClickableTextCell();

  IsCheckboxCell newCheckboxCell();

  IsHyperlinkCell newHyperlinkCell();

  <T, C> IsColumn<T, C> newColumn(ColumnValue<T, C> value, Cell<C> cell);

  IsTextHeader newTextHeader(String text);

  <C> IsHeader<C> newHeader(HeaderValue<C> value, Cell<C> cell);

  <C> IsCompositeCell<C> newCompositeCell(List<HasCell<C, ?>> cells);

}
