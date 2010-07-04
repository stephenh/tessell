package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.HasText;

public class StubHasText implements HasText {

  private String text;

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(final String text) {
    this.text = text;
  }

}
