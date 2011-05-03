package org.gwtmpv.widgets.cellview;

import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;

/** A base stub cell for testing the non-DOM aspects of cell/{@link CellTable}. */
public class StubCell<C> implements Cell<C> {

  /** Stub owners give us a hook for the test to interface interrogate them. */
  public static interface StubCellValue<C> {
    C getValue(int displayedIndex);

    void setValue(int displayedIndex, C value);
  }

  private StubCellValue<C> stubCellValue;

  /** @return the value for this cell at {@code displayedIndex}. */
  public C getValue(int displayedIndex) {
    return stubCellValue().getValue(displayedIndex);
  }

  protected void setValue(int displayedIndex, C value) {
    stubCellValue().setValue(displayedIndex, value);
  }

  /** Set by the {@link StubColumn} so we can interactively interrogate the cell table. */
  public void setStubCellValue(StubCellValue<C> stubCellValue) {
    this.stubCellValue = stubCellValue;
  }

  @Override
  public void render(Cell.Context context, C value, SafeHtmlBuilder sb) {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public boolean isEditing(Cell.Context context, Element parent, C value) {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public void onBrowserEvent(Cell.Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public boolean resetFocus(Cell.Context context, Element parent, C value) {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public void setValue(Cell.Context context, Element parent, C value) {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public boolean dependsOnSelection() {
    return false;
  }

  @Override
  public Set<String> getConsumedEvents() {
    return null;
  }

  @Override
  public boolean handlesSelection() {
    return false;
  }

  private StubCellValue<C> stubCellValue() {
    if (stubCellValue == null) {
      throw new IllegalStateException(this + " has not been configured in a column");
    }
    return stubCellValue;
  }

}
