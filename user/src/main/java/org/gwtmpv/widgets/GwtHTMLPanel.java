package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtHTMLPanel implements IsHTMLPanel {

  private final HTMLPanel panel;

  public GwtHTMLPanel(final HTMLPanel panel) {
    this.panel = panel;
  }

  @Override
  public void add(final IsWidget isWidget) {
    panel.add(isWidget.asWidget());
  }

  @Override
  public boolean remove(final IsWidget isWidget) {
    return panel.remove(isWidget.asWidget());
  }

  @Override
  public int getAbsoluteLeft() {
    return panel.getAbsoluteLeft();
  }

  @Override
  public int getAbsoluteTop() {
    return panel.getAbsoluteTop();
  }

  @Override
  public void onBrowserEvent(final Event event) {
    panel.onBrowserEvent(event);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    panel.fireEvent(event);
  }

  @Override
  public void addStyleName(final String styleName) {
    panel.addStyleName(styleName);
  }

  @Override
  public void removeStyleName(final String styleName) {
    panel.removeStyleName(styleName);
  }

  @Override
  public void clear() {
    panel.clear();
  }

  @Override
  public int getWidgetCount() {
    return panel.getWidgetCount();
  }

  @Override
  public boolean remove(final int index) {
    return panel.remove(index);
  }

  @Override
  public int getOffsetHeight() {
    return panel.getOffsetHeight();
  }

  @Override
  public int getOffsetWidth() {
    return panel.getOffsetWidth();
  }

  @Override
  public void ensureDebugId(final String id) {
    panel.ensureDebugId(id);
  }

  @Override
  public String getStyleName() {
    return panel.getStyleName();
  }

  @Override
  public Widget asWidget() {
    return panel;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(panel.getElement());
  }

  @Override
  public void add(IsWidget widget, IsElement elem) {
    panel.add(widget.asWidget(), elem.asElement());
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new GwtIsWidgetIteratorAdaptor(panel.iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) panel.getWidget(index);
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return panel.getWidgetIndex(child.asWidget());
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, IsElement elem) {
    panel.addAndReplaceElement(widget.asWidget(), elem.asElement());
  }

  @Override
  public void addAndReplaceElement(IsWidget widget, String id) {
    panel.addAndReplaceElement(widget.asWidget(), (Element) panel.getElementById(id));
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return panel.addAttachHandler(handler);
  }

}
