package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tessell.gwt.dom.client.IsElement;

import com.google.gwt.user.client.ui.IsWidget;

public class StubHTMLPanel extends StubComplexPanel implements IsHTMLPanel {

  private final String tag;
  private final String html;
  private final Map<String, IsWidget> replacedById = new HashMap<String, IsWidget>();
  private final Map<IsElement, IsWidget> replacedByElement = new HashMap<IsElement, IsWidget>();
  private final Map<IsElement, List<IsWidget>> addedByElement = new HashMap<IsElement, List<IsWidget>>();

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
    List<IsWidget> added = addedByElement.get(elem);
    if (added == null) {
      added = new ArrayList<IsWidget>();
      addedByElement.put(elem, added);
    }
    added.add(widget);
  }

  public String getTag() {
    return tag;
  }

  public String getHtml() {
    return html;
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, IsElement elem) {
    if (replacedByElement.containsKey(elem)) {
      throw new IllegalStateException(elem + " has already been replaced");
    }
    super.add(widget);
    replacedByElement.put(elem, widget);
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, String id) {
    if (replacedById.containsKey(id)) {
      throw new IllegalStateException(id + " has already been replaced");
    }
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

  public List<IsWidget> getAddedTo(IsElement elem) {
    return addedByElement.get(elem);
  }

  @Override
  public void insert(IsWidget widget, IsElement parent, int beforeIndex, boolean domInsert) {
    super.add(widget);
  }

}
