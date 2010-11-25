package org.gwtmpv.widgets;

import org.gwtmpv.util.cookies.facade.IsCookies;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;

/** A Widget factory. */
public class Widgets {

  private static WidgetsProvider provider;

  static {
    if (GWT.isClient()) {
      setProvider(new GwtWidgetsProvider());
    }
  }

  public static void setProvider(WidgetsProvider provider) {
    Widgets.provider = provider;
  }

  public static IsCookies getCookies() {
    return provider.getCookies();
  }

  public static IsTimer newTimer(Runnable runnable) {
    return provider.newTimer(runnable);
  }

  public static IsWindow getWindow() {
    return provider.getWindow();
  }

  public static IsElement newElement(String tag) {
    return provider.newElement(tag);
  }

  public static IsCheckBox newCheckBox() {
    return provider.newCheckBox();
  }

  public static IsTextBox newTextBox() {
    return provider.newTextBox();
  }

  public static IsPasswordTextBox newPasswordTextBox() {
    return provider.newPasswordTextBox();
  }

  public static IsTextList newTextList() {
    return provider.newTextList();
  }

  public static IsAnchor newAnchor() {
    return provider.newAnchor();
  }

  public static IsHyperlink newHyperline() {
    return provider.newHyperline();
  }

  public static IsInlineHyperlink newInlineHyperlink() {
    return provider.newInlineHyperlink();
  }

  public static IsInlineLabel newInlineLabel() {
    return provider.newInlineLabel();
  }

  public static IsLabel newLabel() {
    return provider.newLabel();
  }

  public static IsImage newImage() {
    return provider.newImage();
  }

  public static IsFlowPanel newFlowPanel() {
    return provider.newFlowPanel();
  }

  public static IsFlowPanel newFlowPanel(IsWidget... widgets) {
    IsFlowPanel p = provider.newFlowPanel();
    for (IsWidget widget : widgets) {
      p.add(widget);
    }
    return p;
  }

  public static IsScrollPanel newScrollPanel() {
    return provider.newScrollPanel();
  }

  public static IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return provider.newTabLayoutPanel(barHeight, barUnit);
  }

  public static IsFadingDialogBox newFadingDialogBox() {
    return provider.newFadingDialogBox();
  }

  public static IsHTML newHTML() {
    return provider.newHTML();
  }

  public static IsHTMLPanel newHTMLPanel(String html) {
    return provider.newHTMLPanel(html);
  }

  public static <T> IsCellTable<T> newCellTable() {
    return provider.newCellTable();
  }

  public static <T> IsCellTable<T> newCellTable(int pageSize, Resources resources) {
    return provider.newCellTable(pageSize, resources);
  }

  public static <T> IsCellList<T> newCellList(Cell<T> cell) {
    return provider.newCellList(cell);
  }

  public static IsPopupPanel newPopupPanel() {
    return provider.newPopupPanel();
  }

  public static IsFocusPanel newFocusPanel() {
    return provider.newFocusPanel();
  }

}
