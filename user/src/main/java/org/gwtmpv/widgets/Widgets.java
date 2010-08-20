package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.CellTable.Resources;

public interface Widgets {

  IsElement newElement(String tag);

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineLabel newInlineLabel();

  IsImage newImage();

  IsFlowPanel newFlowPanel();

  <T> IsCellTable<T> newCellTable();

  <T> IsCellTable<T> newCellTable(int pageSize, Resources resources);

  <T> IsCellList<T> newCellList(Cell<T> cell);

}
