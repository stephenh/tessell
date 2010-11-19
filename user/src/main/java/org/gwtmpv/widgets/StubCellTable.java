package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.util.ObjectUtils;
import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;
import org.gwtmpv.widgets.cellview.StubColumn;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;

public class StubCellTable<T> extends StubWidget implements IsCellTable<T> {

  private final List<IsColumn<T, ?>> columns = new ArrayList<IsColumn<T, ?>>();
  private final List<IsHeader<?>> headers = new ArrayList<IsHeader<?>>();
  private final List<IsHeader<?>> footers = new ArrayList<IsHeader<?>>();
  private final List<T> data = new ArrayList<T>();
  private int redraws = 0;

  public StubCellTable() {
  }

  public StubCellTable(int pageSize) {
    // set page size
  }

  /** @return the stub headers for testing. */
  public IsHeader<?> getHeader(int index) {
    return headers.get(index);
  }

  /** @return the stub footers for testing. */
  public IsHeader<?> getFooter(int index) {
    return footers.get(index);
  }

  /** @return the stub column for testing. */
  public StubColumn<T, ?> getColumn(int index) {
    return (StubColumn<T, ?>) columns.get(index);
  }

  public String getValues(int displayedIndex) {
    String values = "";
    for (int i = 0; i < columns.size(); i++) {
      values += ObjectUtils.toStr(getColumn(i).getValue(displayedIndex), "null");
      if (i < columns.size() - 1) {
        values += " || ";
      }
    }
    return values;
  }

  @Override
  public void setRowData(final int start, final List<? extends T> values) {
    for (int i = 0; i < values.size(); i++) {
      if (i < data.size()) {
        data.set(i, values.get(i));
      } else {
        data.add(start + i, values.get(i));
      }
    }
  }

  @Override
  public void setRowCount(final int size, final boolean isExact) {
  }

  public List<T> getData() {
    return data;
  }

  @Override
  public void addColumn(final IsColumn<T, ?> col) {
    addColumn(col, null, null);
  }

  @Override
  public void addColumn(final IsColumn<T, ?> col, final IsHeader<?> header) {
    addColumn(col, header, null);
  }

  @Override
  public void addColumn(final IsColumn<T, ?> col, final IsHeader<?> header, final IsHeader<?> footer) {
    ((StubColumn<T, ?>) col).setStubCellTable(this);
    columns.add(col);
    headers.add(header); // could be null
    footers.add(footer); // could be null
  }

  @Override
  public List<T> getDisplayedItems() {
    return data;
  }

  @Override
  public void redraw() {
    redraws++;
  }

  @Override
  public void setPageSize(final int pageSize) {
  }

  @Override
  public void redrawHeaders() {
  }

  @Override
  public void redrawFooters() {
  }

  @Override
  public SelectionModel<? super T> getSelectionModel() {
    return null;
  }

  @Override
  public void setSelectionModel(SelectionModel<? super T> selectionModel) {
  }

  @Override
  public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
  }

  @Override
  public HandlerRegistration addRangeChangeHandler(Handler handler) {
    return handlers.addHandler(RangeChangeEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addRowCountChangeHandler(com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
    return handlers.addHandler(RowCountChangeEvent.getType(), handler);
  }

  @Override
  public int getRowCount() {
    return 0;
  }

  @Override
  public Range getVisibleRange() {
    return null;
  }

  @Override
  public boolean isRowCountExact() {
    return false;
  }

  @Override
  public void setRowCount(int count) {
  }

  @Override
  public void setVisibleRange(int start, int length) {
  }

  @Override
  public void setVisibleRange(Range range) {
  }

  @Override
  public void removeColumn(IsColumn<T, ?> col) {
    removeColumn(columns.indexOf(col));
  }

  @Override
  public void removeColumn(int index) {
    columns.remove(index);
    headers.remove(index);
    footers.remove(index);
  }

  public void resetRedraws() {
    redraws = 0;
  }

  public int getRedraws() {
    return redraws;
  }

}
