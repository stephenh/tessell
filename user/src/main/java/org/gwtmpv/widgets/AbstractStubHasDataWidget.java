package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ExposedHasDataPresenter;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;

public class AbstractStubHasDataWidget<T> extends StubWidget implements IsAbstractHasData<T> {

  private final ExposedHasDataPresenter<T> presenter;
  private int redraws = 0;

  public AbstractStubHasDataWidget() {
    this(15);
  }

  public AbstractStubHasDataWidget(int pageSize) {
    presenter = new ExposedHasDataPresenter<T>(this, pageSize, null);
  }

  public void resetRedraws() {
    redraws = 0;
  }

  public int getRedraws() {
    return redraws;
  }

  @Override
  public void setRowData(List<? extends T> values) {
    setRowCount(values.size());
    setVisibleRange(0, values.size());
    setRowData(0, values);
  }

  @Override
  public SelectionModel<? super T> getSelectionModel() {
    return presenter.getSelectionModel();
  }

  @Override
  public T getVisibleItem(int indexOnPage) {
    return presenter.getVisibleItem(indexOnPage);
  }

  @Override
  public int getVisibleItemCount() {
    return presenter.getVisibleItemCount();
  }

  @Override
  public List<T> getVisibleItems() {
    return presenter.getVisibleItems();
  }

  @Override
  public void setRowData(int start, List<? extends T> values) {
    presenter.setRowData(start, values);
  }

  @Override
  public void setSelectionModel(SelectionModel<? super T> selectionModel) {
    presenter.setSelectionModel(selectionModel);
  }

  @Override
  public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
    presenter.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
  }

  @Override
  public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
    return presenter.addRangeChangeHandler(handler);
  }

  @Override
  public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
    return presenter.addRowCountChangeHandler(handler);
  }

  @Override
  public int getRowCount() {
    return presenter.getRowCount();
  }

  @Override
  public Range getVisibleRange() {
    return presenter.getVisibleRange();
  }

  @Override
  public boolean isRowCountExact() {
    return presenter.isRowCountExact();
  }

  @Override
  public void setRowCount(int count) {
    presenter.setRowCount(count);
  }

  @Override
  public void setRowCount(int count, boolean isExact) {
    presenter.setRowCount(count, isExact);
  }

  @Override
  public void setVisibleRange(int start, int length) {
    presenter.setVisibleRange(start, length);
  }

  @Override
  public void setVisibleRange(Range range) {
    presenter.setVisibleRange(range);
  }

  @Override
  public HandlerRegistration addCellPreviewHandler(com.google.gwt.view.client.CellPreviewEvent.Handler<T> handler) {
    return presenter.addCellPreviewHandler(handler);
  }

  @Override
  public ProvidesKey<T> getKeyProvider() {
    return presenter.getKeyProvider();
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
    return presenter.getKeyboardPagingPolicy();
  }

  @Override
  public void setKeyboardPagingPolicy(KeyboardPagingPolicy policy) {
    presenter.setKeyboardPagingPolicy(policy);
  }

  @Override
  public KeyboardSelectionPolicy getKeyboardSelectionPolicy() {
    return presenter.getKeyboardSelectionPolicy();
  }

  @Override
  public void setKeyboardSelectionPolicy(KeyboardSelectionPolicy policy) {
    presenter.setKeyboardSelectionPolicy(policy);
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

}
