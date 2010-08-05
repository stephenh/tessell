package org.gwtmpv.widgets;

public class StubWidgets implements Widgets {

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
  public <T> IsCellTable<T> newCellTable() {
    return new StubCellTable<T>();
  }

}
