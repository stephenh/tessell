package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.safehtml.shared.SafeHtml;

public class HTMLPanel extends com.google.gwt.user.client.ui.HTMLPanel implements IsHTMLPanel {

  public HTMLPanel(String html) {
    super(html);
  }

  public HTMLPanel(SafeHtml html) {
    super(html);
  }

  public HTMLPanel(String tag, String html) {
    super(tag, html);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public void add(com.google.gwt.user.client.ui.IsWidget widget, IsElement elem) {
    add(widget.asWidget(), elem.asElement());
  }

  @Override
  public void addAndReplaceElement(com.google.gwt.user.client.ui.IsWidget widget, IsElement elem) {
    addAndReplaceElement(widget.asWidget(), elem.asElement());
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget widget, IsElement parent, int beforeIndex, boolean domInsert) {
    insert(widget.asWidget(), parent.asElement(), beforeIndex, domInsert);
  }

}
