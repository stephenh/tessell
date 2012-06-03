package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.ColumnsPanel;
import org.tessell.widgets.IsWidget;

/** An interface for adding {@link IsWidget}s in several columns, see {@link ColumnsPanel}. */
public interface IsColumnsPanel extends IsFlowPanel {

  void add(int column, IsWidget isWidget);

  String getColumnStyleName();

  void setColumnStyleName(String columnStyleName);

}
