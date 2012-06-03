package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.safehtml.shared.SafeHtml;

@OtherTypes(intf = IsHTMLPanel.class, stub = StubHTMLPanel.class)
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
  public void add(IsWidget isWidget) {
    add(isWidget.asWidget());
  }

  @Override
  public boolean remove(IsWidget isWidget) {
    return remove(isWidget.asWidget());
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
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
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

  @Override
  public void add(IsWidget widget, IsElement elem) {
    add(widget.asWidget(), elem.asElement());
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, IsElement elem) {
    addAndReplaceElement(widget.asWidget(), elem.asElement());
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, String id) {
    addAndReplaceElement(widget.asWidget(), id);
  }

}
