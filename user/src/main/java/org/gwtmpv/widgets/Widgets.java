package org.gwtmpv.widgets;

import com.google.gwt.user.cellview.client.CellTable.Resources;

public interface Widgets {

  IsHyperlink newHyperline();

  IsInlineHyperlink newInlineHyperlink();

  IsInlineLabel newInlineLabel();

  <T> IsCellTable<T> newCellTable();

  <T> IsCellTable<T> newCellTable(int pageSize, Resources resources);
}
