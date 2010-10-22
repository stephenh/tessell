package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;

/** A Widget factory. */
public class GwtWidgetsProvider implements WidgetsProvider {

  @Override
  public IsElement newElement(String tag) {
    return new GwtElement(DOM.createElement(tag));
  }

  @Override
  public IsTextBox newTextBox() {
    return new GwtTextBox();
  }

  @Override
  public IsTextList newTextList() {
    return new TextList();
  }

  @Override
  public IsAnchor newAnchor() {
    return new GwtAnchor();
  }

  @Override
  public IsHyperlink newHyperline() {
    return new GwtHyperlink();
  }

  @Override
  public IsInlineHyperlink newInlineHyperlink() {
    return new GwtInlineHyperlink();
  }

  @Override
  public IsInlineLabel newInlineLabel() {
    return new GwtInlineLabel();
  }

  @Override
  public IsImage newImage() {
    return new GwtImage();
  }

  @Override
  public IsFlowPanel newFlowPanel() {
    return new GwtFlowPanel();
  }

  @Override
  public IsScrollPanel newScrollPanel() {
    return new GwtScrollPanel();
  }

  @Override
  public IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return new GwtTabLayoutPanel(barHeight, barUnit);
  }

  @Override
  public IsFadingDialogBox newFadingDialogBox() {
    return new GwtFadingDialogBox();
  }

  @Override
  public IsHTML newHTML() {
    return new GwtHTML();
  }

  @Override
  public IsHTMLPanel newHTMLPanel(String html) {
    return new GwtHTMLPanel(new HTMLPanel(html));
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
    return new GwtCheckBox();
  }

}
