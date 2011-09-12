package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;

/** GWT's {@link HasDataPresenter} is DOM coupled, so this is a mediocre attempt at reproducing it's behavior. */
public class StubAbstractHasDataWidget<T> extends StubWidget implements IsAbstractHasData<T> {

  private final List<T> rowData = new ArrayList<T>();
  private int redraws = 0;
  private int rowCount = 0;
  private boolean rowCountExact = false;
  private Range visibleRange;

  public StubAbstractHasDataWidget() {
    this(15);
  }

  public StubAbstractHasDataWidget(int pageSize) {
    setPageSize(pageSize);
  }

  public void resetRedraws() {
    redraws = 0;
  }

  public int getRedraws() {
    return redraws;
  }

  @Override
  public void setRowData(List<? extends T> values) {
    setRowData(0, values);
  }

  @Override
  public SelectionModel<? super T> getSelectionModel() {
    return null;
  }

  @Override
  public T getVisibleItem(int indexOnPage) {
    return getVisibleItems().get(indexOnPage);
  }

  @Override
  public int getVisibleItemCount() {
    return Math.min(visibleRange.getLength(), rowData.size() - visibleRange.getStart());
  }

  @Override
  public List<T> getVisibleItems() {
    return rowData.subList(visibleRange.getStart(), Math.min(rowData.size(), visibleRange.getStart() + visibleRange.getLength()));
  }

  @Override
  public void setRowData(final int start, final List<? extends T> values) {
    // copy/paste from HasDataPresenter
    int valuesLength = values.size();
    int valuesEnd = start + valuesLength;

    // Calculate the bounded start (inclusive) and end index (exclusive).
    int pageStart = getPageStart();
    int pageEnd = getPageStart() + getPageSize();
    int boundedStart = Math.max(start, pageStart);
    int boundedEnd = Math.min(valuesEnd, pageEnd);
    if (start != pageStart && boundedStart >= boundedEnd) {
      // The data is out of range for the current page.
      // Intentionally allow empty lists that start on the page start.
      return;
    }

    // Create placeholders up to the specified index.
    // PendingState<T> pending = ensurePendingState();
    // int cacheOffset = Math.max(0, boundedStart - pageStart - getVisibleItemCount());
    // for (int i = 0; i < cacheOffset; i++) {
    //  pending.rowData.add(null);
    //}

    // Insert the new values into the data array.
    for (int i = boundedStart; i < boundedEnd; i++) {
      T value = values.get(i - start);
      int dataIndex = i - pageStart;
      if (dataIndex < getVisibleItemCount()) {
        rowData.set(dataIndex, value);
      } else {
        rowData.add(value);
      }
    }

    // Remember the range that has been replaced.
    // pending.replaceRange(boundedStart - cacheOffset, boundedEnd);

    // Fire a row count change event after updating the data.
    if (valuesEnd > getRowCount()) {
      setRowCount(valuesEnd, isRowCountExact());
    }
  }

  @Override
  public void setSelectionModel(SelectionModel<? super T> selectionModel) {
  }

  @Override
  public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
  }

  @Override
  public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
    return handlers.addHandler(RangeChangeEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
    return handlers.addHandler(RowCountChangeEvent.getType(), handler);
  }

  @Override
  public int getRowCount() {
    return rowCount;
  }

  @Override
  public Range getVisibleRange() {
    return visibleRange;
  }

  @Override
  public boolean isRowCountExact() {
    return rowCountExact;
  }

  @Override
  public void setRowCount(final int count) {
    setRowCount(count, true);
  }

  @Override
  public void setRowCount(final int count, final boolean isExact) {
    rowCount = count;
    rowCountExact = isExact;
    // Update the pager.
    RowCountChangeEvent.fire(this, count, isExact);
  }

  @Override
  public void setVisibleRange(int start, int length) {
    setVisibleRange(new Range(start, length));
  }

  @Override
  public void setVisibleRange(Range range) {
    this.visibleRange = range;
  }

  @Override
  public HandlerRegistration addCellPreviewHandler(CellPreviewEvent.Handler<T> handler) {
    return null;
  }

  @Override
  public ProvidesKey<T> getKeyProvider() {
    return null;
  }

  @Override
  public int getTabIndex() {
    return 0;
  }

  @Override
  public void setAccessKey(char key) {
  }

  @Override
  public void setFocus(boolean focused) {
  }

  @Override
  public void setTabIndex(int index) {
  }

  @Override
  public KeyboardPagingPolicy getKeyboardPagingPolicy() {
    return null;
  }

  @Override
  public void setKeyboardPagingPolicy(KeyboardPagingPolicy policy) {
  }

  @Override
  public KeyboardSelectionPolicy getKeyboardSelectionPolicy() {
    return null;
  }

  @Override
  public void setKeyboardSelectionPolicy(KeyboardSelectionPolicy policy) {
  }

  @Override
  public int getPageSize() {
    return getVisibleRange().getLength();
  }

  @Override
  public int getPageStart() {
    return 0;
  }

  @Override
  public void setPageStart(int pageStart) {
    setVisibleRange(pageStart, getPageSize());
  }

  @Override
  public void setPageSize(int pageSize) {
    setVisibleRange(getPageStart(), pageSize);
  }

  @Override
  public void redraw() {
    redraws++;
  }

}
