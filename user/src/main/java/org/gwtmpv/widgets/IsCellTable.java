package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.HasData;

public interface IsCellTable<T> extends IsWidget, HasData<T> {

  void addColumn(Column<T, ?> col);

  void addColumn(Column<T, ?> col, Header<?> header);

  void addColumn(Column<T, ?> col, Header<?> header, Header<?> footer);

  void setRowData(int start, List<T> values);

  void setRowCount(int size, boolean isExact);

  void setPageSize(int pageSize);

  List<T> getDisplayedItems();

  void redraw();

  void redrawHeaders();

  void redrawFooters();

}
