package org.tessell.widgets.cellview;

import com.google.gwt.cell.client.HasCell;
import com.google.gwt.user.cellview.client.Column;

public interface IsColumn<T, C> extends HasCell<T, C> {

  Column<T, C> asColumn();

  boolean isSortable();

  void setSortable(boolean sortable);

  IsColumn<T, C> sorted();

}
