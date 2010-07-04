package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.event.shared.GwtEvent;
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
  public void add(final Widget w) {
    panel.add(w);
  }

  @Override
  public void clear() {
    panel.clear();
  }

  @Override
  public Iterator<Widget> iterator() {
    return panel.iterator();
  }

  @Override
  public boolean remove(final Widget w) {
    return panel.remove(w);
  }

  @Override
  public Widget getWidget(final int index) {
    return panel.getWidget(index);
  }

  @Override
  public int getWidgetCount() {
    return panel.getWidgetCount();
  }

  @Override
  public int getWidgetIndex(final Widget child) {
    return panel.getWidgetIndex(child);
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

}