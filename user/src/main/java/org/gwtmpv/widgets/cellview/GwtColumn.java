package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/** Adapts the generic {@link ColumnValue} into the regular GWT {@link Column} hierarchy. */
public class GwtColumn<T, C> extends Column<T, C> implements FieldUpdater<T, C>, IsColumn<T, C> {

  private final ColumnValue<T, C> value;

  public GwtColumn(final ColumnValue<T, C> value, final Cell<C> cell) {
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

  @Override
  public Column<T, C> asColumn() {
    return this;
  }

}
