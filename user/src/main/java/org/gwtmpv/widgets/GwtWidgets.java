package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.DOM;

public class GwtWidgets implements Widgets {

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
  public IsElement newElement(String tag) {
    return new GwtElement(DOM.createElement(tag));
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
  public IsFadingDialogBox newFadingDialogBox() {
    return new GwtFadingDialogBox();
  }

  @Override
  public IsTabLayoutPanel newTabLayoutPanel(double barHeight, Unit barUnit) {
    return new GwtTabLayoutPanel(barHeight, barUnit);
  }

}
