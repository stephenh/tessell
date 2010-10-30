package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;

public class StubCellTable<T> extends StubWidget implements IsCellTable<T> {

  private final List<Column<T, ?>> columns = new ArrayList<Column<T, ?>>();
  private final List<Header<?>> headers = new ArrayList<Header<?>>();
  private final List<Header<?>> footers = new ArrayList<Header<?>>();
  private final List<T> data = new ArrayList<T>();

  public StubCellTable() {
  }

  public StubCellTable(int pageSize) {
    // set page size
  }

  @Override
  public void setRowData(final int start, final List<T> values) {
    for (int i = 0; i < values.size(); i++) {
      data.add(start + i, values.get(i));
    }
  }

  @Override
  public void setRowCount(final int size, final boolean isExact) {
  }

  public Header<?> getHeader(final int column) {
    return headers.get(column);
  }

  public String getRendered(final int row, final int column) {
    final SafeHtmlBuilder sb = new SafeHtmlBuilder();
    columns.get(column).render(data.get(row), null, sb);
    return sb.toString();
  }

  public List<String> getRendered(final int row) {
    final List<String> s = new ArrayList<String>();
    for (int i = 0; i < columns.size(); i++) {
      final SafeHtmlBuilder sb = new SafeHtmlBuilder();
      columns.get(i).render(data.get(row), null, sb);
      s.add(sb.toSafeHtml().asString());
    }
    return s;
  }

  public List<T> getData() {
    return data;
  }

  @Override
  public void addColumn(final Column<T, ?> col) {
    addColumn(col, null, null);
  }

  @Override
  public void addColumn(final Column<T, ?> col, final Header<?> header) {
    addColumn(col, header, null);
  }

  @Override
  public void addColumn(final Column<T, ?> col, final Header<?> header, final Header<?> footer) {
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

}
