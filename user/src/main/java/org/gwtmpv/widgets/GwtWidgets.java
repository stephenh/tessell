package org.gwtmpv.widgets;


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

}
