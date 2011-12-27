package org.tessell.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

/**
 * The header version of {@link GwtColumn}.
 *
 * This header automatically sets itself as its own {@link ValueUpdater}, so if
 * the {@link HeaderValue#set(Object)} method is overridden, then it will be called
 * as appropriate.
 */
public class GwtHeader<C> extends ExposedUpdaterHeader<C> implements ValueUpdater<C>, IsHeader<C> {

  private final HeaderValue<C> value;

  public GwtHeader(final HeaderValue<C> value, final Cell<C> cell) {
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

  @Override
  public Header<C> asHeader() {
    return this;
  }

}
