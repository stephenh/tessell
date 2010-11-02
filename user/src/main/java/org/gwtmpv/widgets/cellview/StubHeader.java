package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class StubHeader<C> implements IsHeader<C> {

  private final HeaderValue<C> value;
  private final Cell<C> cell;

  public StubHeader(HeaderValue<C> value, Cell<C> cell) {
    this.value = value;
    this.cell = cell;
  }

  @Override
  public Header<C> asHeader() {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public Cell<C> getCell() {
    return cell;
  }

  @Override
  public C getValue() {
    return value.get();
  }

  @Override
  public ValueUpdater<C> getUpdater() {
    return new ValueUpdater<C>() {
      public void update(C value) {
        StubHeader.this.value.set(value);
      }
    };
  }

}
