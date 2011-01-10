package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

public class StubRowTable extends StubWidget implements IsRowTable {

  private final List<IsWidget> headers = new ArrayList<IsWidget>();
  private final List<IsWidget> rows = new ArrayList<IsWidget>();

  @Override
  public void addHeader(final IsWidget isWidget) {
    headers.add(isWidget);
  }

  @Override
  public void addRow(final IsWidget isWidget) {
    rows.add(isWidget);
  }

  public List<IsWidget> getHeaders() {
    return headers;
  }

  public List<IsWidget> getRows() {
    return rows;
  }

  @Override
  public void replaceRow(final int i, final IsWidget isWidget) {
    rows.remove(i);
    rows.add(i, isWidget);
  }

  @Override
  public void removeRow(final int i) {
    rows.remove(i);
  }

  @Override
  public void removeRow(IsWidget view) {
    removeRow(rows.indexOf(view));
  }

  @Override
  public int size() {
    return rows.size();
  }

  @Override
  public void insertRow(final int i, final IsWidget isWidget) {
    rows.add(i, isWidget);
  }

}
