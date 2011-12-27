package org.tessell.widgets.cellview;

import com.google.gwt.user.cellview.client.Header;

public class StubTextHeader extends StubHeader<String> implements IsTextHeader {

  private final String text;

  public StubTextHeader(String text) {
    super(new ConstantHeaderValue<String>(text), new StubTextCell());
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public Header<String> asHeader() {
    throw new IllegalStateException("This is a stub");
  }

}
