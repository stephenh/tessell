package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;

/** A Widget factory. */
public class Widgets {

  public static IsElement newElement(String tag) {
    return GWT.isClient() ? new GwtElement(DOM.createElement(tag)) : new StubElement();
  }

  public static IsTextBox newTextBox() {
    return GWT.isClient() ? new GwtTextBox() : new StubTextBox();
  }

  public static IsTextList newTextList() {
    return GWT.isClient() ? new TextList() : new StubTextList();
  }

  public static IsAnchor newAnchor() {
    return GWT.isClient() ? new GwtAnchor() : new StubAnchor();
  }

  public static IsHyperlink newHyperline() {
    return GWT.isClient() ? new GwtHyperlink() : new StubHyperlink();
  }

  public static IsInlineHyperlink newInlineHyperlink() {
    return GWT.isClient() ? new GwtInlineHyperlink() : new StubInlineHyperlink();
  }

  public static IsInlineLabel newInlineLabel() {
    return GWT.isClient() ? new GwtInlineLabel() : new StubInlineLabel();
  }

  public static IsImage newImage() {
    return GWT.isClient() ? new GwtImage() : new StubImage();
  }

  public static IsFlowPanel newFlowPanel() {
    return GWT.isClient() ? new GwtFlowPanel() : new StubFlowPanel();
  }

  public static IsScrollPanel newScrollPanel() {
    return GWT.isClient() ? new GwtScrollPanel() : new StubScrollPanel();
  }

  public static IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return GWT.isClient() ? new GwtTabLayoutPanel(barHeight, barUnit) : new StubTabLayoutPanel();
  }

  public static IsFadingDialogBox newFadingDialogBox() {
    return GWT.isClient() ? new GwtFadingDialogBox() : new StubFadingDialogBox();
  }

  public static IsHTML newHTML() {
    return GWT.isClient() ? new GwtHTML() : new StubHTML();
  }

  public static IsHTMLPanel newHTMLPanel(String html) {
    return GWT.isClient() ? new GwtHTMLPanel(new HTMLPanel(html)) : new StubHTMLPanel(html);
  }

  public static <T> IsCellTable<T> newCellTable() {
    return GWT.isClient() ? new GwtCellTable<T>() : new StubCellTable<T>();
  }

  public static <T> IsCellTable<T> newCellTable(int pageSize, Resources resources) {
    return GWT.isClient() ? new GwtCellTable<T>(pageSize, resources) : new StubCellTable<T>(pageSize);
  }

  public static <T> IsCellList<T> newCellList(Cell<T> cell) {
    return GWT.isClient() ? new GwtCellList<T>(cell) : new StubCellList<T>(cell);
  }

}
