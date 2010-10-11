package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class DelegateIsWidget implements IsWidget {

  protected Widget widget;

  protected void setWidget(final Widget widget) {
    this.widget = widget;
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void ensureDebugId(final String id) {
    widget.ensureDebugId(id);
  }

  @Override
  public int getAbsoluteLeft() {
    return widget.getAbsoluteLeft();
  }

  @Override
  public int getAbsoluteTop() {
    return widget.getAbsoluteTop();
  }

  @Override
  public int getOffsetHeight() {
    return widget.getOffsetHeight();
  }

  @Override
  public int getOffsetWidth() {
    return widget.getOffsetWidth();
  }

  @Override
  public void onBrowserEvent(final Event event) {
    widget.onBrowserEvent(event);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    widget.fireEvent(event);
  }

  @Override
  public void addStyleName(final String styleName) {
    widget.addStyleName(styleName);
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public String getStyleName() {
    return widget.getStyleName();
  }

  @Override
  public void removeStyleName(final String styleName) {
    widget.removeStyleName(styleName);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(widget.getElement());
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return widget.addAttachHandler(handler);
  }

}
