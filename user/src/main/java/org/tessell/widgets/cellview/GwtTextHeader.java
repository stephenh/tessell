package org.tessell.widgets.cellview;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;

public class GwtTextHeader extends TextHeader implements IsTextHeader {

  public GwtTextHeader(String text) {
    super(text);
  }

  @Override
  public Header<String> asHeader() {
    return this;
  }

  @Override
  public ValueUpdater<String> getUpdater() {
    return null;
  }

}
