package org.gwtmpv.widgets;

import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;

public interface IsCellTable<T> extends IsWidget, IsAbstractHasData<T> {

  void addColumn(IsColumn<T, ?> col);

  void addColumn(IsColumn<T, ?> col, String headerString);

  void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header, IsHeader<?> footer);

  void addColumn(IsColumn<T, ?> col, String headerString, String footerString);

  void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml);

  void setColumnWidth(IsColumn<T, ?> col, String width);

  void setColumnWidth(IsColumn<T, ?> col, double width, Unit unit);

  void setTableLayoutFixed(boolean isFixed);

  HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);

  int getColumnCount();

  IsColumn<T, ?> getIsColumn(int col);

  int getColumnIndex(IsColumn<T, ?> column);

  ColumnSortList getColumnSortList();

  void removeColumn(IsColumn<T, ?> col);

  void removeColumn(int index);

  void redrawHeaders();

  void redrawFooters();

  void setWidth(String width, boolean isFixedLayout);

}
