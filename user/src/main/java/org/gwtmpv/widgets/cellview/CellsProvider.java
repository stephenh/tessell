package org.gwtmpv.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public interface CellsProvider {

  IsTextCell newTextCell();

  IsClickableTextCell newClickableTextCell();

  IsCheckboxCell newCheckboxCell();

  IsHyperlinkCell newHyperlinkCell();

  IsSafeHtmlCell newSafeHtmlCell();

  <C> IsHtmlCell<C> newHtmlCell(SafeHtmlRenderer<C> renderer);

  <T, C> IsColumn<T, C> newColumn(ColumnValue<T, C> value, Cell<C> cell);

  IsTextHeader newTextHeader(String text);

  IsSafeHtmlHeader newSafeHtmlHeader(SafeHtml html);

  <C> IsHeader<C> newHeader(HeaderValue<C> value, Cell<C> cell);

  <C> IsCompositeCell<C> newCompositeCell(List<HasCell<C, ?>> cells);

}
