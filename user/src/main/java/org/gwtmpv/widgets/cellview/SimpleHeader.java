package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;

/**
 * The header version of {@link DerivedColumn}. Both need better names.
 *
 * Instead of sub-classing header, the user can pass in a {@link SimpleValue}.
 * 
 * This header automatically sets itself as its own {@link ValueUpdater}, so if
 * the {@link SimpleValue#set(Object)} method is overridden, then it will be called
 * as appropriate.
 */
public class SimpleHeader<C> extends ExposedUpdaterHeader<C> implements ValueUpdater<C> {

  private final SimpleValue<C> value;

  public SimpleHeader(final SimpleValue<C> value, final Cell<C> cell) {
    super(cell);
    this.value = value;
    setUpdater(this);
  }

  @Override
  public C getValue() {
    return value.get();
  }

  @Override
  public void update(final C value) {
    this.value.set(value);
  }

}
