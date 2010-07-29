package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;

public interface IsCellTable<T> extends IsWidget {

  void addColumn(Column<T, ?> col);

  void addColumn(Column<T, ?> col, Header<?> header);

  void addColumn(Column<T, ?> col, Header<?> header, Header<?> footer);

  void setData(int start, int length, List<T> values);

  List<T> getDisplayedItems();

  void refreshFooters();

  void refreshHeaders();

  void redraw();

}
