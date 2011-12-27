package org.tessell.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.SafeHtmlRenderer;

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
    return new StubClickableTextCell();
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

  @Override
  public IsHyperlinkCell newHyperlinkCell() {
    return new StubHyperlinkCell();
  }

  @Override
  public <C> IsHtmlCell<C> newHtmlCell(SafeHtmlRenderer<C> renderer) {
    return new StubHtmlCell<C>(renderer);
  }

  @Override
  public IsSafeHtmlCell newSafeHtmlCell() {
    return new StubSafeHtmlCell();
  }

  @Override
  public IsSafeHtmlHeader newSafeHtmlHeader(SafeHtml html) {
    return new StubSafeHtmlHeader(html);
  }

  @Override
  public IsClickableSafeHtmlCell newClickableSafeHtmlCell() {
    return new StubClickableSafeHtmlCell();
  }

}
