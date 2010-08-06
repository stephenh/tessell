package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;

public class GwtCellList<T> extends CellList<T> implements IsCellList<T> {

  public GwtCellList(Cell<T> cell) {
    super(cell);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
