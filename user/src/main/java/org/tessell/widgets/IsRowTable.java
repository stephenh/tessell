package org.tessell.widgets;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.util.ListDiff.ListLike;

public interface IsRowTable extends IsWidget, HasRows {

  ListLike<IsWidget> getRowsPanel();

}
