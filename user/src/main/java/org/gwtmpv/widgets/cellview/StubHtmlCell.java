package org.gwtmpv.widgets.cellview;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class StubHtmlCell<C> extends StubCell<C> implements IsHtmlCell<C> {

  private final SafeHtmlRenderer<C> renderer;

  public StubHtmlCell(SafeHtmlRenderer<C> renderer) {
    this.renderer = renderer;
  }

  public SafeHtml render(int displayedIndex) {
    return renderer.render(getValue(displayedIndex));
  }

}
