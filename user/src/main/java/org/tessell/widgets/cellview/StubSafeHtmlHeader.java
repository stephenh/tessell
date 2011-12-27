package org.tessell.widgets.cellview;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Header;

public class StubSafeHtmlHeader extends StubHeader<SafeHtml> implements IsSafeHtmlHeader {

  private final SafeHtml html;

  public StubSafeHtmlHeader(SafeHtml html) {
    super(new ConstantHeaderValue<SafeHtml>(html), new StubSafeHtmlCell());
    this.html = html;
  }

  public SafeHtml getHtml() {
    return html;
  }

  @Override
  public Header<SafeHtml> asHeader() {
    throw new IllegalStateException("This is a stub");
  }

}
