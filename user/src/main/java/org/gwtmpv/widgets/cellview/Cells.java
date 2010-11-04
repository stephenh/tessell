package org.gwtmpv.widgets.cellview;

import java.util.List;

import org.bindgen.BindingRoot;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;

/**
 * Factory methods for cellview-related classes.
 *
 * Uses GWT cells (DOM coupled) by default, call {@link StubCellsProvider#install()}
 * to use stub cells.
 */
public class Cells {

  private static CellsProvider provider;

  static {
    if (GWT.isClient()) {
      setProvider(new GwtCellsProvider());
    }
  }

  public static void setProvider(CellsProvider provider) {
    Cells.provider = provider;
  }

  public static IsHeader<Object> newCompositeHeader(IsHeader<?>... headers) {
    return new CompositeHeader(headers);
  }

  public static <T, C> IsColumn<T, C> newColumn(ColumnValue<T, C> value, Cell<C> cell) {
    return provider.newColumn(value, cell);
  }

  public static <T, C> BoundColumnValue<T, C> boundValue(BindingRoot<T, C> binding) {
    return new BoundColumnValue<T, C>(binding);
  }

  public static <C> IsHeader<C> newHeader(HeaderValue<C> value, Cell<C> cell) {
    return provider.newHeader(value, cell);
  }

  public static IsTextCell newTextCell() {
    return provider.newTextCell();
  }

  public static IsClickableTextCell newClickableTextCell() {
    return provider.newClickableTextCell();
  }

  public static IsCheckboxCell newCheckboxCell() {
    return provider.newCheckboxCell();
  }

  public static IsTextHeader newTextHeader(String text) {
    return provider.newTextHeader(text);
  }

  public static <C> IsCompositeCell<C> newCompositeCell(List<HasCell<C, ?>> cells) {
    return provider.newCompositeCell(cells);
  }

  public static IsHyperlinkCell newHyperlinkCell() {
    return provider.newHyperlinkCell();
  }
}
