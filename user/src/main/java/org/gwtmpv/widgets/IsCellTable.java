package org.gwtmpv.widgets;

import java.util.List;

import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;

import com.google.gwt.view.client.HasData;

public interface IsCellTable<T> extends IsWidget, HasData<T> {

  void addColumn(IsColumn<T, ?> col);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header, IsHeader<?> footer);

  void removeColumn(IsColumn<T, ?> col);

  void removeColumn(int index);

  void setRowData(int start, List<T> values);

  void setRowCount(int size, boolean isExact);

  void setPageSize(int pageSize);

  List<T> getDisplayedItems();

  void redraw();

  void redrawHeaders();

  void redrawFooters();

}
