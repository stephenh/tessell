package org.tessell.bootstrap;

import org.tessell.bootstrap.views.StubButtonView;

public class StubButton extends Button {

  protected final StubButtonView view = (StubButtonView) widget;

  public String tooltip;

  public void click() {
    view.button().click();
  }

  @Override
  public void setTooltip(final String tooltip) {
    this.tooltip = tooltip;
  }

  public StubButtonView getView() {
    return view;
  }
}
