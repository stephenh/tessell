package org.gwtmpv.widgets;

import org.gwtmpv.widgets.cellview.StubCell;
import org.gwtmpv.widgets.cellview.StubCell.StubCellValue;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;

public class StubCellList<T> extends AbstractStubHasDataWidget<T> implements IsCellList<T> {

  @SuppressWarnings("unused")
  private final Cell<T> cell;
  private SafeHtml emptyListMessage;
  private ValueUpdater<T> valueUpdater;

  public StubCellList(Cell<T> cell) {
    this.cell = cell;
    ((StubCell<T>) cell).setStubCellValue(new StubCellValue<T>() {
      public T getValue(int displayedIndex) {
        return getVisibleItem(displayedIndex);
      }

      @Override
      public void setValue(int displayedIndex, T value) {
        // odd that displayedIndex is ignored
        valueUpdater.update(value);
      }
    });
  }

  @Override
  public SafeHtml getEmptyListMessage() {
    return emptyListMessage;
  }

  @Override
  public void setEmptyListMessage(SafeHtml safeHtml) {
    this.emptyListMessage = safeHtml;
  }

  @Override
  public void setValueUpdater(ValueUpdater<T> valueUpdater) {
    this.valueUpdater = valueUpdater;
  }

}
