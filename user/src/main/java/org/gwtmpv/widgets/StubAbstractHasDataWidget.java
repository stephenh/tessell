package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.cellview.client.PublicHasDataPresenter;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;

public class StubAbstractHasDataWidget<T> extends StubWidget implements IsAbstractHasData<T> {

  private final PublicHasDataPresenter<T> p;
  private int redraws = 0;

  public StubAbstractHasDataWidget() {
    this(15);
  }

  public StubAbstractHasDataWidget(int pageSize) {
    p = new PublicHasDataPresenter<T>(this, new FakeView(), pageSize, null);
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
    // setRowCount(values.size());
    // setVisibleRange(0, values.size());
    setRowData(0, values);
  }

  @Override
  public SelectionModel<? super T> getSelectionModel() {
    return p.getSelectionModel();
  }

  @Override
  public T getVisibleItem(int indexOnPage) {
    return getVisibleItems().get(indexOnPage);
  }

  @Override
  public int getVisibleItemCount() {
    return p.getVisibleItemCount();
  }

  @Override
  public List<T> getVisibleItems() {
    return p.getVisibleItems();
  }

  @Override
  public void setRowData(final int start, final List<? extends T> values) {
    StubScheduler.get().runWithDeferred(new Runnable() {
      public void run() {
        p.setRowData(start, values);
      }
    });
  }

  @Override
  public void setSelectionModel(SelectionModel<? super T> selectionModel) {
    p.setSelectionModel(selectionModel);
  }

  @Override
  public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
    p.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
  }

  @Override
  public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
    return p.addRangeChangeHandler(handler);
  }

  @Override
  public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
    return p.addRowCountChangeHandler(handler);
  }

  @Override
  public int getRowCount() {
    return p.getRowCount();
  }

  @Override
  public Range getVisibleRange() {
    return p.getVisibleRange();
  }

  @Override
  public boolean isRowCountExact() {
    return p.isRowCountExact();
  }

  @Override
  public void setRowCount(final int count) {
    StubScheduler.get().runWithDeferred(new Runnable() {
      public void run() {
        p.setRowCount(count);
      }
    });
  }

  @Override
  public void setRowCount(final int count, final boolean isExact) {
    StubScheduler.get().runWithDeferred(new Runnable() {
      public void run() {
        p.setRowCount(count, isExact);
      }
    });
  }

  @Override
  public void setVisibleRange(int start, int length) {
    setVisibleRange(new Range(start, length));
  }

  @Override
  public void setVisibleRange(Range range) {
    p.setVisibleRange(range);
  }

  @Override
  public HandlerRegistration addCellPreviewHandler(CellPreviewEvent.Handler<T> handler) {
    return p.addCellPreviewHandler(handler);
  }

  @Override
  public ProvidesKey<T> getKeyProvider() {
    return p.getKeyProvider();
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
    return p.getKeyboardPagingPolicy();
  }

  @Override
  public void setKeyboardPagingPolicy(KeyboardPagingPolicy policy) {
    p.setKeyboardPagingPolicy(policy);
  }

  @Override
  public KeyboardSelectionPolicy getKeyboardSelectionPolicy() {
    return p.getKeyboardSelectionPolicy();
  }

  @Override
  public void setKeyboardSelectionPolicy(KeyboardSelectionPolicy policy) {
    p.setKeyboardSelectionPolicy(policy);
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

  private final class FakeView extends PublicHasDataPresenter.PublicView<T> {
    public <H extends EventHandler> HandlerRegistration addHandler(H handler, Type<H> type) {
      return null;
    }

    @Override
    public void render(SafeHtmlBuilder sb, List<T> values, int start, SelectionModel<? super T> selectionModel) {
    }

    @Override
    public void replaceAllChildren(List<T> values, SafeHtml html, boolean stealFocus, boolean other) {
    }

    @Override
    public void replaceChildren(List<T> values, int start, SafeHtml html, boolean stealFocus) {
    }

    @Override
    public void resetFocus() {
    }

    @Override
    public void setKeyboardSelected(int index, boolean selected, boolean stealFocus) {
    }

    @Override
    public void setLoadingState(LoadingState state) {
    }
  }

}
