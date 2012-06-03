package org.tessell.widgets;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.IsTimer;
import org.tessell.gwt.user.client.IsWindow;
import org.tessell.gwt.user.client.ui.IsAbsolutePanel;
import org.tessell.gwt.user.client.ui.IsAnchor;
import org.tessell.gwt.user.client.ui.IsButton;
import org.tessell.gwt.user.client.ui.IsCheckBox;
import org.tessell.gwt.user.client.ui.IsDataGrid;
import org.tessell.gwt.user.client.ui.IsDockLayoutPanel;
import org.tessell.gwt.user.client.ui.IsFlowPanel;
import org.tessell.gwt.user.client.ui.IsFocusPanel;
import org.tessell.gwt.user.client.ui.IsHTML;
import org.tessell.gwt.user.client.ui.IsHTMLPanel;
import org.tessell.gwt.user.client.ui.IsHyperlink;
import org.tessell.gwt.user.client.ui.IsImage;
import org.tessell.gwt.user.client.ui.IsInlineHTML;
import org.tessell.gwt.user.client.ui.IsInlineHyperlink;
import org.tessell.gwt.user.client.ui.IsInlineLabel;
import org.tessell.gwt.user.client.ui.IsLabel;
import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.gwt.user.client.ui.IsPasswordTextBox;
import org.tessell.gwt.user.client.ui.IsPopupPanel;
import org.tessell.gwt.user.client.ui.IsResizeLayoutPanel;
import org.tessell.gwt.user.client.ui.IsScrollPanel;
import org.tessell.gwt.user.client.ui.IsSimplePanel;
import org.tessell.gwt.user.client.ui.IsSuggestBox;
import org.tessell.gwt.user.client.ui.IsTabLayoutPanel;
import org.tessell.gwt.user.client.ui.IsTextBox;
import org.tessell.place.history.IsHistory;
import org.tessell.util.cookies.facade.IsCookies;
import org.tessell.widgets.cellview.IsCellList;
import org.tessell.widgets.cellview.IsCellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface WidgetsProvider {

  IsCookies getCookies();

  IsTimer newTimer(Runnable runnable);

  IsAnimation newAnimation(AnimationLogic logic);

  IsWindow getWindow();

  IsHistory getHistory();

  IsAbsolutePanel getRootPanel();

  IsDockLayoutPanel newDockLayoutPanel(Unit unit);

  IsSimplePanel newSimplePanel();

  IsLabel newLabel();

  IsListBox newListBox();

  IsElement newElement(String tag);

  IsTextBox newTextBox();

  IsPasswordTextBox newPasswordTextBox();

  IsTextList newTextList();

  IsCheckBox newCheckBox();

  IsAnchor newAnchor();

  IsButton newButton();

  IsPopupPanel newPopupPanel();

  IsAbsolutePanel newAbsolutePanel();

  IsFocusPanel newFocusPanel();

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineHTML newInlineHTML();

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

  <T> IsDataGrid<T> newDataGrid();

  <T> IsDataGrid<T> newDataGrid(int pageSize, DataGrid.Resources resources);

  <T> IsCellList<T> newCellList(Cell<T> cell);

  IsSuggestBox newSuggestBox(SuggestOracle oracle);

  IsResizeLayoutPanel newResizeLayoutPanel();

}
