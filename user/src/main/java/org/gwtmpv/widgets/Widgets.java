package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;

/** A Widget factory. */
public class Widgets {

  public static WidgetsProvider provider = new GwtWidgetsProvider();

  public static IsElement newElement(String tag) {
    return provider.newElement(tag);
  }

  public static IsCheckBox newCheckBox() {
    return provider.newCheckBox();
  }

  public static IsTextBox newTextBox() {
    return provider.newTextBox();
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

  public static IsImage newImage() {
    return provider.newImage();
  }

  public static IsFlowPanel newFlowPanel() {
    return provider.newFlowPanel();
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

}
