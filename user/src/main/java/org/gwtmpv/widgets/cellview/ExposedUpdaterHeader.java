package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

/**
 * Exposes a header's {@link ValueUpdater}.
 * 
 * To make composite headers, {@code CompositeCell} needs a header's {@link ValueUpdater} to make fake {@link FieldUpdater}s., which is not exposed by default.
 *
 * @param <C> the cell type
 */
public abstract class ExposedUpdaterHeader<C> extends Header<C> {

  public ExposedUpdaterHeader(final Cell<C> cell) {
    super(cell);
  }

  private ValueUpdater<C> updater;

  @Override
  public void setUpdater(final ValueUpdater<C> updater) {
    // still do the super call for the base class onBrowserEvent method
    super.setUpdater(updater);
    this.updater = updater;
  }

  public ValueUpdater<C> getUpdater() {
    return updater;
  }
}
