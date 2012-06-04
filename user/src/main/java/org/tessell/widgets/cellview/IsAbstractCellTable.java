package org.tessell.widgets.cellview;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.widgets.IsAbstractHasData;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.RowStyles;

public interface IsAbstractCellTable<T> extends IsWidget, IsAbstractHasData<T> {

  void addColumn(IsColumn<T, ?> col);

  void addColumn(IsColumn<T, ?> col, String headerString);

  void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header, IsHeader<?> footer);

  void addColumn(IsColumn<T, ?> col, String headerString, String footerString);

  void addColumn(IsColumn<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml);

  void setColumnWidth(IsColumn<T, ?> col, String width);

  void setColumnWidth(IsColumn<T, ?> col, double width, Unit unit);

  HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);

  int getColumnCount();

  IsColumn<T, ?> getIsColumn(int col);

  int getColumnIndex(IsColumn<T, ?> column);

  ColumnSortList getColumnSortList();

  void removeColumn(IsColumn<T, ?> col);

  void removeColumn(int index);

  void redrawHeaders();

  void redrawFooters();

  void setRowStyles(RowStyles<T> rowStyles);

}
