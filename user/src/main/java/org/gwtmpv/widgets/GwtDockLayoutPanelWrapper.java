package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtDockLayoutPanelWrapper implements IsDockLayoutPanel {

  private final DockLayoutPanel panel;

  public GwtDockLayoutPanelWrapper(DockLayoutPanel panel) {
    this.panel = panel;
  }

  @Override
  public boolean remove(IsWidget isWidget) {
    return panel.remove(isWidget.asWidget());
  }

  @Override
  public int getAbsoluteTop() {
    return panel.getAbsoluteTop();
  }

  @Override
  public int getAbsoluteLeft() {
    return panel.getAbsoluteLeft();
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
  public void ensureDebugId(String id) {
    panel.ensureDebugId(id);
  }

  @Override
  public boolean isAttached() {
    return panel.isAttached();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(panel.getElement());
  }

  @Override
  public Widget asWidget() {
    return panel;
  }

  @Override
  public void onBrowserEvent(Event event) {
    panel.onBrowserEvent(event);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    panel.fireEvent(event);
  }

  @Override
  public void addStyleName(String styleName) {
    panel.addStyleName(styleName);
  }

  @Override
  public void removeStyleName(String styleName) {
    panel.removeStyleName(styleName);
  }

  @Override
  public String getStyleName() {
    return panel.getStyleName();
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return panel.addAttachHandler(handler);
  }

  @Override
  public void clear() {
    panel.clear();
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
  public int getWidgetCount() {
    return panel.getWidgetCount();
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return panel.getWidgetIndex(child.asWidget());
  }

  @Override
  public boolean remove(int index) {
    return panel.remove(index);
  }

  @Override
  public void onResize() {
    panel.onResize();
  }

  @Override
  public void animate(int duration) {
    panel.animate(duration);
  }

  @Override
  public void animate(int duration, AnimationCallback callback) {
    panel.animate(duration, callback);
  }

  @Override
  public void forceLayout() {
    panel.forceLayout();
  }

  @Override
  public void add(IsWidget widget) {
    panel.add(widget.asWidget());
  }

  @Override
  public void addEast(IsWidget widget, double size) {
    panel.addEast(widget.asWidget(), size);
  }

  @Override
  public void addLineEnd(IsWidget widget, double size) {
    panel.addLineEnd(widget.asWidget(), size);
  }

  @Override
  public void addLineStart(IsWidget widget, double size) {
    panel.addLineStart(widget.asWidget(), size);
  }

  @Override
  public void addNorth(IsWidget widget, double size) {
    panel.addNorth(widget.asWidget(), size);
  }

  @Override
  public void addSouth(IsWidget widget, double size) {
    panel.addSouth(widget.asWidget(), size);
  }

  @Override
  public void addWest(IsWidget widget, double size) {
    panel.addWest(widget.asWidget(), size);
  }

  @Override
  public void insertEast(IsWidget widget, double size, IsWidget before) {
    panel.insertEast(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineEnd(IsWidget widget, double size, IsWidget before) {
    panel.insertLineEnd(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineStart(IsWidget widget, double size, IsWidget before) {
    panel.insertLineStart(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertNorth(IsWidget widget, double size, IsWidget before) {
    panel.insertNorth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertSouth(IsWidget widget, double size, IsWidget before) {
    panel.insertSouth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertWest(IsWidget widget, double size, IsWidget before) {
    panel.insertWest(widget.asWidget(), size, before.asWidget());
  }

}
