package org.tessell.widgets;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.util.ListDiff.ListLike;

import com.google.gwt.user.client.ui.Widget;

public interface IsRowTable extends IsWidget, HasRows {

  ListLike<IsWidget> getRowsPanel();

  /** Assumes {@code row} is a table row and returns its index */
  int indexOfRow(final Widget row);

  /** Assumes {@code row} is a table row and returns its index */
  int indexOfRow(final IsWidget row);

}
