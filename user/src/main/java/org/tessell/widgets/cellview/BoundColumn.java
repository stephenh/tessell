package org.tessell.widgets.cellview;

import org.bindgen.BindingRoot;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * Provides the "value getting" part of table columns by wrapping a bindgen-generated binding.
 *
 * @param <T> the row type (root binding type in bindgen)
 * @param <C> the value type
 */
public class BoundColumn<T, C> extends Column<T, C> implements FieldUpdater<T, C> {

  private final BindingRoot<T, C> binding;

  public static <R, P> BoundColumn<R, P> of(final BindingRoot<R, P> binding, final Cell<P> cell) {
    return new BoundColumn<R, P>(binding, cell);
  }

  public BoundColumn(final BindingRoot<T, C> binding, final Cell<C> cell) {
    super(cell);
    this.binding = binding;
    setFieldUpdater(this);
  }

  @Override
  public C getValue(final T row) {
    return binding.getWithRoot(row);
  }

  @Override
  public void update(int index, T object, C value) {
    binding.setWithRoot(object, value);
  }

}
