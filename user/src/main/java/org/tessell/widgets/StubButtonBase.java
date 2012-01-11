package org.tessell.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;

public class StubButtonBase extends StubFocusWidget implements IsButtonBase {

  private String html;
  private String text;

  @Override
  public String getHTML() {
    return html;
  }

  @Override
  public void setHTML(final SafeHtml html) {
    this.html = html.asString();
  }

  @Override
  public void setHTML(final String html) {
    this.html = html;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(final String text) {
    this.text = text;
  }

}
