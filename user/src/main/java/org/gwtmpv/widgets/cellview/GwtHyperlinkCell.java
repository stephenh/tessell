package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class GwtHyperlinkCell extends AbstractCell<IsHyperlinkCell.Data> implements IsHyperlinkCell {

  private static final Templates t = GWT.create(Templates.class);

  public interface Templates extends SafeHtmlTemplates {
    // be nice to eventually handle ids and arbitrary things
    @Template("<a href=\"{0}\">{1}</a>")
    SafeHtml link(String href, String content);
  }

  @Override
  public void render(Data value, Object key, SafeHtmlBuilder sb) {
    sb.append(t.link(value.href, value.content));
  }

}
