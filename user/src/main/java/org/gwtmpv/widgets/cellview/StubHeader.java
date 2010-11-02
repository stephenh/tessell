package org.gwtmpv.widgets.cellview;

import org.gwtmpv.widgets.cellview.StubCell.StubCellValue;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class StubHeader<C> implements IsHeader<C> {

  private final HeaderValue<C> value;
  private final Cell<C> cell;

  public StubHeader(final HeaderValue<C> value, final Cell<C> cell) {
    this.value = value;
    this.cell = cell;
    ((StubCell<C>) cell).setStubCellValue(new StubCellValue<C>() {
      @Override
      public C getValue(int displayedIndex) {
        return value.get();
      }

      @Override
      public void setValue(int displayedIndex, C newValue) {
        value.set(newValue);
      }
    });
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
