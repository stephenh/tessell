package org.tessell.widgets;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.GwtAnimation;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.*;
import org.tessell.gwt.user.client.ui.AbsolutePanel;
import org.tessell.gwt.user.client.ui.Anchor;
import org.tessell.gwt.user.client.ui.Button;
import org.tessell.gwt.user.client.ui.CheckBox;
import org.tessell.gwt.user.client.ui.DataGrid;
import org.tessell.gwt.user.client.ui.DockLayoutPanel;
import org.tessell.gwt.user.client.ui.FlowPanel;
import org.tessell.gwt.user.client.ui.FocusPanel;
import org.tessell.gwt.user.client.ui.GwtAbsolutePanelDelegate;
import org.tessell.gwt.user.client.ui.HTML;
import org.tessell.gwt.user.client.ui.HTMLPanel;
import org.tessell.gwt.user.client.ui.Hyperlink;
import org.tessell.gwt.user.client.ui.Image;
import org.tessell.gwt.user.client.ui.InlineHTML;
import org.tessell.gwt.user.client.ui.InlineHyperlink;
import org.tessell.gwt.user.client.ui.InlineLabel;
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
import org.tessell.gwt.user.client.ui.Label;
import org.tessell.gwt.user.client.ui.ListBox;
import org.tessell.gwt.user.client.ui.PasswordTextBox;
import org.tessell.gwt.user.client.ui.PopupPanel;
import org.tessell.gwt.user.client.ui.ResizeLayoutPanel;
import org.tessell.gwt.user.client.ui.ScrollPanel;
import org.tessell.gwt.user.client.ui.SimplePanel;
import org.tessell.gwt.user.client.ui.SuggestBox;
import org.tessell.gwt.user.client.ui.TabLayoutPanel;
import org.tessell.gwt.user.client.ui.TextBox;
import org.tessell.place.history.GwtHistory;
import org.tessell.place.history.IsHistory;
import org.tessell.widgets.cellview.GwtCellList;
import org.tessell.widgets.cellview.GwtCellTable;
import org.tessell.widgets.cellview.IsCellList;
import org.tessell.widgets.cellview.IsCellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;

/** A Widget factory. */
public class GwtWidgetsProvider implements WidgetsProvider {

  private final GwtWindow window = new GwtWindow();
  private final GwtCookies cookies = new GwtCookies();
  private final GwtHistory history = new GwtHistory();
  private final IsAbsolutePanel root = new GwtAbsolutePanelDelegate(RootPanel.get());

  @Override
  public IsTimer newTimer(Runnable runnable) {
    return new GwtTimer(runnable);
  }

  @Override
  public IsWindow getWindow() {
    return window;
  }

  @Override
  public IsElement newElement(String tag) {
    return new GwtElement(DOM.createElement(tag));
  }

  @Override
  public IsTextBox newTextBox() {
    return new TextBox();
  }

  @Override
  public IsTextList newTextList() {
    return new TextList();
  }

  @Override
  public IsAnchor newAnchor() {
    return new Anchor();
  }

  @Override
  public IsHyperlink newHyperline() {
    return new Hyperlink();
  }

  @Override
  public IsInlineHyperlink newInlineHyperlink() {
    return new InlineHyperlink();
  }

  @Override
  public IsInlineLabel newInlineLabel() {
    return new InlineLabel();
  }

  @Override
  public IsImage newImage() {
    return new Image();
  }

  @Override
  public IsFlowPanel newFlowPanel() {
    return new FlowPanel();
  }

  @Override
  public IsScrollPanel newScrollPanel() {
    return new ScrollPanel();
  }

  @Override
  public IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return new TabLayoutPanel(barHeight, barUnit);
  }

  @Override
  public IsFadingDialogBox newFadingDialogBox() {
    return new FadingDialogBox();
  }

  @Override
  public IsHTML newHTML() {
    return new HTML();
  }

  @Override
  public IsHTMLPanel newHTMLPanel(String html) {
    return new HTMLPanel(html);
  }

  @Override
  public <T> IsCellTable<T> newCellTable() {
    return new GwtCellTable<T>();
  }

  @Override
  public <T> IsCellTable<T> newCellTable(int pageSize, Resources resources) {
    return new GwtCellTable<T>(pageSize, resources);
  }

  @Override
  public <T> IsCellList<T> newCellList(Cell<T> cell) {
    return new GwtCellList<T>(cell);
  }

  @Override
  public IsCheckBox newCheckBox() {
    return new CheckBox();
  }

  @Override
  public IsPasswordTextBox newPasswordTextBox() {
    return new PasswordTextBox();
  }

  @Override
  public IsPopupPanel newPopupPanel() {
    return new PopupPanel();
  }

  @Override
  public IsFocusPanel newFocusPanel() {
    return new FocusPanel();
  }

  @Override
  public IsLabel newLabel() {
    return new Label();
  }

  @Override
  public IsCookies getCookies() {
    return cookies;
  }

  @Override
  public IsAnimation newAnimation(AnimationLogic logic) {
    return new GwtAnimation(logic);
  }

  @Override
  public IsHistory getHistory() {
    return history;
  }

  @Override
  public IsAbsolutePanel newAbsolutePanel() {
    return new AbsolutePanel();
  }

  @Override
  public IsAbsolutePanel getRootPanel() {
    return root;
  }

  @Override
  public IsInlineHTML newInlineHTML() {
    return new InlineHTML();
  }

  @Override
  public IsButton newButton() {
    return new Button();
  }

  @Override
  public IsSuggestBox newSuggestBox(SuggestOracle oracle) {
    return new SuggestBox(oracle);
  }

  @Override
  public IsDockLayoutPanel newDockLayoutPanel(Unit unit) {
    return new DockLayoutPanel(unit);
  }

  @Override
  public IsSimplePanel newSimplePanel() {
    return new SimplePanel();
  }

  @Override
  public IsListBox newListBox() {
    return new ListBox();
  }

  @Override
  public <T> IsDataGrid<T> newDataGrid() {
    return new DataGrid<T>();
  }

  @Override
  public <T> IsDataGrid<T> newDataGrid(int pageSize, DataGrid.Resources resources) {
    return new DataGrid<T>(pageSize, resources);
  }

  @Override
  public IsResizeLayoutPanel newResizeLayoutPanel() {
    return new ResizeLayoutPanel();
  }

}
