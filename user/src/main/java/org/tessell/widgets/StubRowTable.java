package org.tessell.widgets;

import java.util.ArrayList;
import java.util.List;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.util.ListDiff.ListLike;

import com.google.gwt.user.client.ui.Widget;

public class StubRowTable extends StubWidget implements IsRowTable {

  private final List<IsWidget> headers = new ArrayList<IsWidget>();
  private final List<IsWidget> rows = new ArrayList<IsWidget>();

  @Override
  public void addHeader(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    headers.add((IsWidget) isWidget);
  }

  @Override
  public void addRow(final com.google.gwt.user.client.ui.IsWidget isWidget) {
    rows.add((IsWidget) isWidget);
  }

  public List<IsWidget> getHeaders() {
    return headers;
  }

  public List<IsWidget> getRows() {
    return rows;
  }

  @Override
  public void replaceRow(final int i, final com.google.gwt.user.client.ui.IsWidget isWidget) {
    rows.remove(i);
    rows.add(i, (IsWidget) isWidget);
  }

  @Override
  public void removeRow(final int i) {
    rows.remove(i);
  }

  @Override
  public boolean removeRow(com.google.gwt.user.client.ui.IsWidget view) {
    removeRow(rows.indexOf(view));
    return true;
  }

  @Override
  public int size() {
    return rows.size();
  }

  @Override
  public void clearRows() {
    while (rows.size() > 0) {
      removeRow(0);
    }
  }

  @Override
  public void insertRow(final int i, final com.google.gwt.user.client.ui.IsWidget isWidget) {
    rows.add(i, (IsWidget) isWidget);
  }

  @Override
  protected IsWidget findInChildren(String id) {
    IsWidget a = findInChildren(headers.iterator(), id);
    if (a != null) {
      return a;
    }
    return findInChildren(rows.iterator(), id);
  }

  @Override
  public ListLike<IsWidget> getRowsPanel() {
    return new ListLikeAdaptor();
  }

  private class ListLikeAdaptor implements ListLike<IsWidget> {
    @Override
    public IsWidget remove(int index) {
      IsWidget row = rows.get(index);
      removeRow(index);
      return row;
    }

    @Override
    public void add(int index, IsWidget a) {
      insertRow(index, a);
    }
  }

  @Override
  public int indexOfRow(Widget row) {
    return rows.indexOf(row);
  }

  @Override
  public int indexOfRow(IsWidget row) {
    return rows.indexOf(row);
  }
}
