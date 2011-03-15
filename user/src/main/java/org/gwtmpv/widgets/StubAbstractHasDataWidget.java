package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;

public class StubAbstractHasDataWidget<T> extends StubWidget implements IsAbstractHasData<T> {

  private final List<T> data = new ArrayList<T>();
  private SelectionModel<? super T> selectionModel;
  private int redraws = 0;

  public StubAbstractHasDataWidget() {
    this(15);
  }

  public StubAbstractHasDataWidget(int pageSize) {
    // setPageSize(pageSize);
  }

  public void resetRedraws() {
    redraws = 0;
  }

  public int getRedraws() {
    return redraws;
  }

  @Override
  public void setRowData(List<? extends T> values) {
    // setRowCount(values.size());
    // setVisibleRange(0, values.size());
    setRowData(0, values);
  }

  @Override
  public SelectionModel<? super T> getSelectionModel() {
    return selectionModel;
  }

  @Override
  public T getVisibleItem(int indexOnPage) {
    return getVisibleItems().get(indexOnPage);
  }

  @Override
  public int getVisibleItemCount() {
    throw fail();
  }

  @Override
  public List<T> getVisibleItems() {
    return data; // not really visible--just all
  }

  @Override
  public void setRowData(int start, List<? extends T> values) {
    for (int i = 0; i < values.size(); i++) {
      if (i < data.size()) {
        data.set(i, values.get(i));
      } else {
        data.add(start + i, values.get(i));
      }
    }
  }

  @Override
  public void setSelectionModel(SelectionModel<? super T> selectionModel) {
    this.selectionModel = selectionModel;
  }

  @Override
  public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
    throw fail();
  }

  @Override
  public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
    throw fail();
  }

  @Override
  public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
    throw fail();
  }

  @Override
  public int getRowCount() {
    throw fail();
  }

  @Override
  public Range getVisibleRange() {
    throw fail();
  }

  @Override
  public boolean isRowCountExact() {
    throw fail();
  }

  @Override
  public void setRowCount(int count) {
    throw fail();
  }

  @Override
  public void setRowCount(int count, boolean isExact) {
    throw fail();
  }

  @Override
  public void setVisibleRange(int start, int length) {
    throw fail();
  }

  @Override
  public void setVisibleRange(Range range) {
    throw fail();
  }

  @Override
  public HandlerRegistration addCellPreviewHandler(com.google.gwt.view.client.CellPreviewEvent.Handler<T> handler) {
    throw fail();
  }

  @Override
  public ProvidesKey<T> getKeyProvider() {
    throw fail();
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
    throw fail();
  }

  @Override
  public void setKeyboardPagingPolicy(KeyboardPagingPolicy policy) {
    throw fail();
  }

  @Override
  public KeyboardSelectionPolicy getKeyboardSelectionPolicy() {
    throw fail();
  }

  @Override
  public void setKeyboardSelectionPolicy(KeyboardSelectionPolicy policy) {
    throw fail();
  }

  @Override
  public int getPageSize() {
    return getVisibleRange().getLength();
  }

  @Override
  public int getPageStart() {
    return getVisibleRange().getStart();
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

  private RuntimeException fail() {
    return new UnsupportedOperationException("The stub is not fancy enough yet");
  }

}
