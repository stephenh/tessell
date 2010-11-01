package org.gwtmpv.widgets.cellview;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

/**
 * Renders multiple headers as one header.
 *
 * We reuse {@link CompositeCell}'s magic, however it was built assuming it was
 * going to put together composite columns ({@link HasCell}s), which headers
 * are not, so we have to cheat a little bit.
 *
 * <b>Note:</b> that to receive browser events, headers must extend {@link ExposedUpdaterHeader},
 * which allows this implementation to get the header's {@link ValueUpdater}.
 */
public class CompositeHeader {

  /** Static creation method.
   * 
   * <code>
   * CompositeHeader.of(header1, header2);
   * </code>
   */
  public static Header<Object> of(final Header<?>... headers) {
    final List<HasCell<Object, ?>> cells = new ArrayList<HasCell<Object, ?>>();
    for (final Header<?> header : headers) {
      cells.add(newHasCell(header));
    }
    return new Header<Object>(new CompositeCell<Object>(cells)) {
      @Override
      public Object getValue() {
        return null;
      }
    };
  }

  /** Makes a fake {@link HasCell} for {@code header} to satisfy the {@link CompositeCell} cstr. */
  private static <X> HasCell<Object, X> newHasCell(final Header<X> header) {
    return new HasCell<Object, X>() {
      @Override
      public Cell<X> getCell() {
        return header.getCell();
      }

      @Override
      public FieldUpdater<Object, X> getFieldUpdater() {
        if (header instanceof ExposedUpdaterHeader) {
          return new FieldUpdater<Object, X>() {
            public void update(final int index, final Object object, final X value) {
              ((ExposedUpdaterHeader<X>) header).getUpdater().update(value);
            }
          };
        } else {
          return null;
        }
      }

      @Override
      public X getValue(final Object object) {
        return header.getValue();
      }
    };
  }

}
