package org.tessell.widgets.cellview;

import com.google.gwt.safehtml.shared.SafeHtml;

public class StubClickableSafeHtmlCell extends StubCell<SafeHtml> implements IsClickableSafeHtmlCell {

  public void click(int displayedIndex) {
    // seems odd...
    setValue(displayedIndex, getValue(displayedIndex));
  }

}
