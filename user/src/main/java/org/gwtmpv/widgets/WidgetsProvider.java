package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;

public interface WidgetsProvider {

  IsElement newElement(String tag);

  IsTextBox newTextBox();

  IsPasswordTextBox newPasswordTextBox();

  IsTextList newTextList();

  IsCheckBox newCheckBox();

  IsAnchor newAnchor();

  IsPopupPanel newPopupPanel();

  IsFocusPanel newFocusPanel();

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineLabel newInlineLabel();

  IsImage newImage();

  IsFlowPanel newFlowPanel();

  IsScrollPanel newScrollPanel();

  IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit);

  IsFadingDialogBox newFadingDialogBox();

  IsHTML newHTML();

  IsHTMLPanel newHTMLPanel(String html);

  <T> IsCellTable<T> newCellTable();

  <T> IsCellTable<T> newCellTable(int pageSize, CellTable.Resources resources);

  <T> IsCellList<T> newCellList(Cell<T> cell);

}
