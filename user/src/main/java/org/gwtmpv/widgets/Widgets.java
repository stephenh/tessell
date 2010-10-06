package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;

/** A Widget factory interface. */
public interface Widgets {

  IsElement newElement(String tag);

  IsAnchor newAnchor();

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineLabel newInlineLabel();

  IsImage newImage();

  IsFlowPanel newFlowPanel();

  IsScrollPanel newScrollPanel();

  IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit);

  IsFadingDialogBox newFadingDialogBox();

  <T> IsCellTable<T> newCellTable();

  <T> IsCellTable<T> newCellTable(int pageSize, Resources resources);

  <T> IsCellList<T> newCellList(Cell<T> cell);

}
