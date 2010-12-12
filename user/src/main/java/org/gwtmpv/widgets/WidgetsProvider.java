package org.gwtmpv.widgets;

import org.gwtmpv.place.history.IsHistory;
import org.gwtmpv.util.cookies.facade.IsCookies;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;

public interface WidgetsProvider {

  IsCookies getCookies();

  IsTimer newTimer(Runnable runnable);

  IsAnimation newAnimation(AnimationLogic logic);

  IsWindow getWindow();

  IsHistory getHistory();

  IsAbsolutePanel getRootPanel();

  IsLabel newLabel();

  IsElement newElement(String tag);

  IsTextBox newTextBox();

  IsPasswordTextBox newPasswordTextBox();

  IsTextList newTextList();

  IsCheckBox newCheckBox();

  IsAnchor newAnchor();

  IsPopupPanel newPopupPanel();

  IsAbsolutePanel newAbsolutePanel();

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
