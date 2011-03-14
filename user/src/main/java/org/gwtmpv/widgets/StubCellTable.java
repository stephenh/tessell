package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.util.ObjectUtils;
import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;
import org.gwtmpv.widgets.cellview.StubColumn;

public class StubCellTable<T> extends AbstractStubHasDataWidget<T> implements IsCellTable<T> {

  private final List<IsColumn<T, ?>> columns = new ArrayList<IsColumn<T, ?>>();
  private final List<IsHeader<?>> headers = new ArrayList<IsHeader<?>>();
  private final List<IsHeader<?>> footers = new ArrayList<IsHeader<?>>();

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

}
