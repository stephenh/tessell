package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;

public class GwtSafeHtmlHeader extends SafeHtmlHeader implements IsSafeHtmlHeader {

  public GwtSafeHtmlHeader(SafeHtml text) {
    super(text);
  }

  @Override
  public ValueUpdater<SafeHtml> getUpdater() {
    return null;
  }

  @Override
  public Header<SafeHtml> asHeader() {
    return this;
  }

}
