package org.gwtmpv.widgets;

public interface Widgets {

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineLabel newInlineLabel();

  <T> IsCellTable<T> newCellTable();

}
