package org.tessell.gwt.user.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.tessell.gwt.dom.client.IsElement;

import com.google.gwt.user.client.ui.IsWidget;

public class StubHTMLPanel extends StubComplexPanel implements IsHTMLPanel {

  private final String tag;
  private final String html;
  private final Map<String, IsWidget> replacedById = new HashMap<String, IsWidget>();
  private final Map<IsElement, IsWidget> replacedByElement = new HashMap<IsElement, IsWidget>();

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
    replacedByElement.put(elem, widget);
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, String id) {
    super.add(widget);
    replacedById.put(id, widget);
  }

  public IsWidget getReplaced(String id) {
    IsWidget w = replacedById.get(id);
    if (w == null) {
      throw new IllegalArgumentException("Could not find widget for " + id);
    }
    return w;
  }

  public IsWidget getReplaced(IsElement elem) {
    IsWidget w = replacedByElement.get(elem);
    if (w == null) {
      throw new IllegalArgumentException("Could not find widget for " + elem);
    }
    return w;
  }

  @Override
  public void insert(IsWidget widget, IsElement parent, int beforeIndex, boolean domInsert) {
    super.add(widget);
  }

}
