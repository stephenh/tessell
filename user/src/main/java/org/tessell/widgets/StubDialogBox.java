package org.tessell.widgets;

public class StubDialogBox extends StubPopupPanel implements IsDialogBox {

  private String caption;

  @Override
  public String getHTML() {
    return caption;
  }

  @Override
  public void setHTML(final String html) {
    caption = html;
  }

  @Override
  public String getText() {
    return caption;
  }

  @Override
  public void setText(final String text) {
    caption = text;
  }

}
