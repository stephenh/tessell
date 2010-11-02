package org.gwtmpv.widgets;

import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;

public class GwtCellTable<T> extends CellTable<T> implements IsCellTable<T> {

  public GwtCellTable() {
  }

  public GwtCellTable(int pageSize, Resources resources) {
    super(pageSize, resources);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public void addColumn(IsColumn<T, ?> col) {
    addColumn(col.asColumn());
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, IsHeader<?> header) {
    addColumn(col.asColumn(), header.asHeader());
  }

  @Override
  public void addColumn(IsColumn<T, ?> col, IsHeader<?> header, IsHeader<?> footer) {
    addColumn(col.asColumn(), header.asHeader(), footer.asHeader());
  }

}
