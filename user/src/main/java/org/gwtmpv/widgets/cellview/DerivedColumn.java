package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * A column that gets/sets it's value from a user-defined Value function,
 * to avoid sub-classing column all of the time.
 *
 * This column also automatically configures itself as it's own {@link FieldUpdater},
 * so if the {@link DerivedColumn.Value} implementation overrides {@code set}, then
 * it will get called appropriately.
 */
public class DerivedColumn<T, C> extends Column<T, C> implements FieldUpdater<T, C> {

  /**
   * Static creation method to avoid template parameters.
   * 
   * @param <T> the row type
   * @param <C> the cell type
   * @param value how to convert the {@code T} row to our {@code C} value
   * @param cell how to render our {@code C} value to HTML
   */
  public static <T, C> DerivedColumn<T, C> from(final DerivedColumn.Value<T, C> value, final Cell<C> cell) {
    return new DerivedColumn<T, C>(value, cell);
  }

  /** Base class for user's to provide a {@code get} and optionally {@code set} implementation. */
  public static abstract class Value<T, C> {
    public abstract C get(T object);

    public void set(final T object, final C value) {
      throw new UnsupportedOperationException(this + " is read only");
    }
  }

  private final Value<T, C> value;

  public DerivedColumn(final Value<T, C> value, final Cell<C> cell) {
    super(cell);
    this.value = value;
    setFieldUpdater(this);
  }

  @Override
  public C getValue(final T object) {
    return value.get(object);
  }

  @Override
  public void update(final int index, final T object, final C value) {
    this.value.set(object, value);
  }

}
