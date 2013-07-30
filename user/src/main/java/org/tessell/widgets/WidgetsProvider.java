package org.tessell.widgets;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.IsCookies;
import org.tessell.gwt.user.client.IsTimer;
import org.tessell.gwt.user.client.IsWindow;
import org.tessell.gwt.user.client.ui.*;
import org.tessell.place.history.IsHistory;
import org.tessell.widgets.cellview.IsCellList;
import org.tessell.widgets.cellview.IsCellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
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

  IsElementWidget newElementWidget(String tag);

  IsElementWidget newElementWidget(IsElement element);

  IsTextArea newTextArea();

  IsTextBox newTextBox();

  IsPasswordTextBox newPasswordTextBox();

  IsTextList newTextList();

  IsCheckBox newCheckBox();

  IsSimpleCheckBox newSimpleCheckBox();

  IsRadioButton newRadioButton(String name);

  IsSimpleRadioButton newSimpleRadioButton(String name);

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

  IsHTMLPanel newHTMLPanel(String tag, String html);

  <T> IsCellTable<T> newCellTable();

  <T> IsCellTable<T> newCellTable(int pageSize, CellTable.Resources resources);

  <T> IsDataGrid<T> newDataGrid();

  <T> IsDataGrid<T> newDataGrid(int pageSize, DataGrid.Resources resources);

  <T> IsCellList<T> newCellList(Cell<T> cell);

  IsSuggestBox newSuggestBox(SuggestOracle oracle);

  IsSuggestBox newSuggestBox(SuggestOracle oracle, IsTextBoxBase box);

  IsSuggestBox newSuggestBox(SuggestOracle oracle, IsTextBoxBase box, SuggestionDisplay display);

  IsResizeLayoutPanel newResizeLayoutPanel();

}
