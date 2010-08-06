package org.gwtmpv.widgets;

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

}
