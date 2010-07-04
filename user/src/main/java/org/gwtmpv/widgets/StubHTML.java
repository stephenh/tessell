package org.gwtmpv.widgets;

public class StubHTML extends StubLabel implements IsHTML {

  @Override
  public String getHTML() {
    return getText();
  }

  @Override
  public void setHTML(final String html) {
    setText(html);
  }

}
