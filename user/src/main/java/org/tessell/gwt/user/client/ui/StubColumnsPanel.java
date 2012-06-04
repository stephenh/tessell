package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;

public class StubColumnsPanel extends StubFlowPanel implements IsColumnsPanel {

  private final ArrayList<ArrayList<IsWidget>> columns = new ArrayList<ArrayList<IsWidget>>();

  @Override
  public void add(final int column, final IsWidget isWidget) {
    while (column >= columns.size()) {
      columns.add(new ArrayList<IsWidget>());
    }
    columns.get(column).add(isWidget);
  }

  public IsWidget get(final int column, final int index) {
    return columns.get(column).get(index);
  }

  public ArrayList<ArrayList<IsWidget>> getColumns() {
    return columns;
  }

  @Override
  public String getColumnStyleName() {
    return null;
  }

  @Override
  public void setColumnStyleName(final String columnStyleName) {
  }

}
