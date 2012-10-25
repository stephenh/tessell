package org.tessell.widgets;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.GwtAnimation;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.*;
import org.tessell.gwt.user.client.ui.*;
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
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
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
  public IsTextArea newTextArea() {
    return new TextArea();
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
  public IsHTMLPanel newHTMLPanel(String tag, String html) {
    return new HTMLPanel(tag, html);
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
  public IsSuggestBox newSuggestBox(SuggestOracle oracle, IsTextBoxBase box) {
    return new SuggestBox(oracle, box.asWidget());
  }

  @Override
  public IsSuggestBox newSuggestBox(SuggestOracle oracle, IsTextBoxBase box, SuggestionDisplay display) {
    return new SuggestBox(oracle, box.asWidget(), display);
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

  @Override
  public IsSimpleCheckBox newSimpleCheckBox() {
    return new SimpleCheckBox();
  }

  @Override
  public IsRadioButton newRadioButton(String name) {
    return new RadioButton(name);
  }

  @Override
  public IsSimpleRadioButton newSimpleRadioButton(String name) {
    return new SimpleRadioButton(name);
  }

}
