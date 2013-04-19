package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newHeaderLineView;

import org.tessell.bootstrap.views.IsHeaderLineView;

public class HeaderLine extends AbstractLine {

  private final IsHeaderLineView view = setWidget(newHeaderLineView());

  public String getTitle() {
    return view.h3().getIsElement().getInnerText();
  }

  public void setTitle(final String title) {
    view.h3().getIsElement().setInnerText(title);
  }

  @Override
  protected void setupDisableWhenActive() {
  }

}
