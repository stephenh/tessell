package org.gwtmpv.widgets;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/** Adds displays to consecutive {@link FlowPanel}s as needed. */
public class ColumnsPanel extends FlowPanel implements IsColumnsPanel {

  private final ArrayList<FlowPanel> columns = new ArrayList<FlowPanel>();
  private String columnStyleName;

  @Override
  public void add(final int column, final IsWidget isWidget) {
    while (column >= columns.size()) {
      final FlowPanel p = new FlowPanel();
      if (columnStyleName != null) {
        p.addStyleName(columnStyleName);
      }
      columns.add(p);
      this.add(p);
    }
    columns.get(column).add(isWidget.asWidget());
  }

  public String getColumnStyleName() {
    return columnStyleName;
  }

  public void setColumnStyleName(final String columnStyleName) {
    this.columnStyleName = columnStyleName;
  }

  @Override
  public void add(final IsWidget isWidget) {
    add(isWidget.asWidget());
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
    return remove(isWidget.asWidget());
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
