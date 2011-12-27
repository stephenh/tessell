package org.tessell.widgets;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;

public class StubCellPanel extends StubComplexPanel implements IsCellPanel {

  private int spacing;

  @Override
  public int getSpacing() {
    return spacing;
  }

  @Override
  public void setBorderWidth(int width) {
  }

  @Override
  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

  @Override
  public void setCellHeight(IsWidget w, String height) {
  }

  @Override
  public void setCellHorizontalAlignment(IsWidget w, HorizontalAlignmentConstant align) {
  }

  @Override
  public void setCellVerticalAlignment(IsWidget w, VerticalAlignmentConstant align) {
  }

  @Override
  public void setCellWidth(IsWidget w, String width) {
  }

}
