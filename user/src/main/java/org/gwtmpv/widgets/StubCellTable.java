package org.gwtmpv.widgets;

import static org.gwtmpv.widgets.cellview.Cells.newSafeHtmlHeader;
import static org.gwtmpv.widgets.cellview.Cells.newTextHeader;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.util.ObjectUtils;
import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;
import org.gwtmpv.widgets.cellview.StubColumn;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;

public class StubCellTable<T> extends StubAbstractHasDataWidget<T> implements IsCellTable<T> {

  private final List<IsColumn<T, ?>> columns = new ArrayList<IsColumn<T, ?>>();
  private final List<IsHeader<?>> headers = new ArrayList<IsHeader<?>>();
  private final List<IsHeader<?>> footers = new ArrayList<IsHeader<?>>();
  private final ColumnSortList sortList = new ColumnSortList();

  public StubCellTable() {
  }

  public StubCellTable(int pageSize) {
    super(pageSize);
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
  public void addColumn(final IsColumn<T, ?> col) {
    addColumn(col, (IsHeader<?>) null, (IsHeader<?>) null);
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
  public void redrawHeaders() {
    redraw();
  }

  @Override
  public void redrawFooters() {
    redraw();
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

  @Override
  public int getColumnCount() {
    return columns.size();
  }

  @Override
  public IsColumn<T, ?> getIsColumn(int col) {
    return columns.get(col);
  }

  @Override
  public int getColumnIndex(IsColumn<T, ?> column) {
    return columns.indexOf(column);
  }

  @Override
  public ColumnSortList getColumnSortList() {
    return sortList;
  }

  @Override
  public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
    return handlers.addHandler(ColumnSortEvent.getType(), handler);
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, String headerString) {
    addColumn(col, newTextHeader(headerString));
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml) {
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, String headerString, String footerString) {
    addColumn(col, newTextHeader(headerString), newTextHeader(footerString));
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml) {
    addColumn(col, newSafeHtmlHeader(headerHtml), newSafeHtmlHeader(footerHtml));
  }

  @Override
  public void setWidth(String width, boolean isFixedLayout) {
  }

  @Override
  public void setColumnWidth(IsColumn<T, ?> col, String width) {
  }

  @Override
  public void setColumnWidth(IsColumn<T, ?> col, double width, Unit unit) {
  }

  @Override
  public void setTableLayoutFixed(boolean isFixed) {
  }

}
