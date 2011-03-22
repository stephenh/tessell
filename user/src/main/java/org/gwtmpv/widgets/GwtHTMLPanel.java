package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;

public class GwtHTMLPanel extends HTMLPanel implements IsHTMLPanel {

  public GwtHTMLPanel(String html) {
    super(html);
  }

  public GwtHTMLPanel(SafeHtml html) {
    super(html);
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
    return new GwtIsWidgetIteratorAdaptor(iterator());
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
