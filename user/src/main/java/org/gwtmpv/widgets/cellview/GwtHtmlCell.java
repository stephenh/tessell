package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class GwtHtmlCell<C> extends AbstractSafeHtmlCell<C> implements IsHtmlCell<C> {

  private final SafeHtml ifNull;

  public GwtHtmlCell(SafeHtmlRenderer<C> renderer) {
    super(renderer);
    this.ifNull = null;
  }

  @Override
  protected void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
    if (data != null) {
      sb.append(data);
    } else if (ifNull != null) {
      sb.append(ifNull);
    }
  }

}
