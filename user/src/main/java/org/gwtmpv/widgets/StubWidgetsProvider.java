package org.gwtmpv.widgets;

import org.gwtmpv.place.history.IsHistory;
import org.gwtmpv.place.history.StubHistory;
import org.gwtmpv.util.cookies.facade.IsCookies;
import org.gwtmpv.util.cookies.facade.StubCookies;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;

/** A Widget factory. */
public class StubWidgetsProvider implements WidgetsProvider {

  public static void install() {
    Widgets.setProvider(new StubWidgetsProvider());
  }

  private final StubWindow window = new StubWindow();
  private final StubCookies cookies = new StubCookies();
  private final StubHistory history = new StubHistory();
  private final StubAbsolutePanel root = new StubAbsolutePanel();

  @Override
  public IsTimer newTimer(Runnable runnable) {
    return new StubTimer(runnable);
  }

  @Override
  public IsAnimation newAnimation(AnimationLogic logic) {
    StubAnimation a = new StubAnimation(logic);
    StubAnimations.captureIfNeeded(a);
    return a;
  }

  @Override
  public IsWindow getWindow() {
    return window;
  }

  @Override
  public IsElement newElement(String tag) {
    return new StubElement();
  }

  @Override
  public IsTextBox newTextBox() {
    return new StubTextBox();
  }

  @Override
  public IsTextList newTextList() {
    return new StubTextList();
  }

  @Override
  public IsAnchor newAnchor() {
    return new StubAnchor();
  }

  @Override
  public IsHyperlink newHyperline() {
    return new StubHyperlink();
  }

  @Override
  public IsInlineHyperlink newInlineHyperlink() {
    return new StubInlineHyperlink();
  }

  @Override
  public IsInlineLabel newInlineLabel() {
    return new StubInlineLabel();
  }

  @Override
  public IsImage newImage() {
    return new StubImage();
  }

  @Override
  public IsFlowPanel newFlowPanel() {
    return new StubFlowPanel();
  }

  @Override
  public IsScrollPanel newScrollPanel() {
    return new StubScrollPanel();
  }

  @Override
  public IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return new StubTabLayoutPanel();
  }

  @Override
  public IsFadingDialogBox newFadingDialogBox() {
    return new StubFadingDialogBox();
  }

  @Override
  public IsHTML newHTML() {
    return new StubHTML();
  }

  @Override
  public IsHTMLPanel newHTMLPanel(String html) {
    return new StubHTMLPanel(html);
  }

  @Override
  public <T> IsCellTable<T> newCellTable() {
    return new StubCellTable<T>();
  }

  @Override
  public <T> IsCellTable<T> newCellTable(int pageSize, Resources resources) {
    return new StubCellTable<T>(pageSize);
  }

  @Override
  public <T> IsCellList<T> newCellList(Cell<T> cell) {
    return new StubCellList<T>(cell);
  }

  @Override
  public IsCheckBox newCheckBox() {
    return new StubCheckBox();
  }

  @Override
  public IsPasswordTextBox newPasswordTextBox() {
    return new StubPasswordTextBox();
  }

  @Override
  public IsPopupPanel newPopupPanel() {
    return new StubPopupPanel();
  }

  @Override
  public IsFocusPanel newFocusPanel() {
    return new StubFocusPanel();
  }

  @Override
  public IsLabel newLabel() {
    return new StubLabel();
  }

  @Override
  public IsCookies getCookies() {
    return cookies;
  }

  @Override
  public IsHistory getHistory() {
    return history;
  }

  @Override
  public IsAbsolutePanel newAbsolutePanel() {
    return new StubAbsolutePanel();
  }

  @Override
  public IsAbsolutePanel getRootPanel() {
    return root;
  }

}
