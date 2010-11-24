package org.gwtmpv.widgets.cellview;

import java.util.ArrayList;
import java.util.List;

import org.bindgen.BindingRoot;
import org.gwtmpv.model.properties.Property;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.SafeHtmlRenderer;

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

  public static <T, C> ColumnValue<T, C> constantValue(final C value) {
    return new ColumnValue<T, C>() {
      public C get(T object) {
        return value;
      }
    };
  }

  public static <T> ColumnValue<T, T> identityValue() {
    return new ColumnValue<T, T>() {
      public T get(T object) {
        return object;
      }
    };
  }

  public static <T, C> BoundColumnProperty<T, C> boundProperty(BindingRoot<T, ? extends Property<C>> binding) {
    return new BoundColumnProperty<T, C>(binding);
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

  public static <T> IsColumn<T, ?> newCompositeColumn(HasCell<T, ?> c1, HasCell<T, ?> c2) {
    List<HasCell<T, ?>> cells = new ArrayList<HasCell<T, ?>>();
    cells.add(c1);
    cells.add(c2);
    return newColumn(Cells.<T> identityValue(), newCompositeCell(cells));
  }

  public static <T> IsColumn<T, ?> newCompositeColumn(HasCell<T, ?> c1, HasCell<T, ?> c2, HasCell<T, ?> c3) {
    List<HasCell<T, ?>> cells = new ArrayList<HasCell<T, ?>>();
    cells.add(c1);
    cells.add(c2);
    cells.add(c3);
    return newColumn(Cells.<T> identityValue(), newCompositeCell(cells));
  }

  public static <T> IsColumn<T, ?> newCompositeColumn(HasCell<T, ?> c1, HasCell<T, ?> c2, HasCell<T, ?> c3, HasCell<T, ?> c4) {
    List<HasCell<T, ?>> cells = new ArrayList<HasCell<T, ?>>();
    cells.add(c1);
    cells.add(c2);
    cells.add(c3);
    cells.add(c4);
    return newColumn(Cells.<T> identityValue(), newCompositeCell(cells));
  }

  public static <T> IsColumn<T, ?> newCompositeColumn(HasCell<T, ?> c1, HasCell<T, ?> c2, HasCell<T, ?> c3, HasCell<T, ?> c4, HasCell<T, ?> c5) {
    List<HasCell<T, ?>> cells = new ArrayList<HasCell<T, ?>>();
    cells.add(c1);
    cells.add(c2);
    cells.add(c3);
    cells.add(c4);
    cells.add(c5);
    return newColumn(Cells.<T> identityValue(), newCompositeCell(cells));
  }

  public static IsHyperlinkCell newHyperlinkCell() {
    return provider.newHyperlinkCell();
  }

  public static <C> IsHtmlCell<C> newHtmlCell(SafeHtmlRenderer<C> renderer) {
    return provider.newHtmlCell(renderer);
  }

}
