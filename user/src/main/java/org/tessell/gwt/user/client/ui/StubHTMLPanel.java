package org.tessell.gwt.user.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.tessell.gwt.dom.client.IsElement;

import com.google.gwt.user.client.ui.IsWidget;

public class StubHTMLPanel extends StubComplexPanel implements IsHTMLPanel {

  private final String tag;
  private final String html;
  private final Map<String, IsWidget> replaced = new HashMap<String, IsWidget>();

  public StubHTMLPanel() {
    this("div", null);
  }

  public StubHTMLPanel(String html) {
    this("div", html);
  }

  public StubHTMLPanel(String tag, String html) {
    this.tag = tag;
    this.html = html;
  }

  @Override
  public void add(IsWidget widget, IsElement elem) {
    super.add(widget);
  }

  public String getTag() {
    return tag;
  }

  public String getHtml() {
    return html;
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, IsElement elem) {
    super.add(widget);
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, String id) {
    super.add(widget);
    replaced.put(id, widget);
  }

  public IsWidget getReplaced(String id) {
    IsWidget w = replaced.get(id);
    if (w == null) {
      throw new IllegalArgumentException("Could not find " + id);
    }
    return w;
  }

  @Override
  public void insert(IsWidget widget, IsElement parent, int beforeIndex, boolean domInsert) {
    super.add(widget);
  }

}
