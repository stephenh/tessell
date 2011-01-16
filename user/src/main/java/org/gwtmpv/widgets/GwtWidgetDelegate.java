package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class GwtWidgetDelegate implements IsWidget {

  private final Widget delegate;

  public GwtWidgetDelegate(Widget delegate) {
    this.delegate = delegate;
  }

  @Override
  public int getAbsoluteTop() {
    return delegate.getAbsoluteTop();
  }

  @Override
  public int getAbsoluteLeft() {
    return delegate.getAbsoluteLeft();
  }

  @Override
  public int getOffsetHeight() {
    return delegate.getOffsetHeight();
  }

  @Override
  public int getOffsetWidth() {
    return delegate.getOffsetWidth();
  }

  @Override
  public void ensureDebugId(String id) {
    delegate.ensureDebugId(id);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(delegate.getElement());
  }

  @Override
  public Widget asWidget() {
    return delegate;
  }

  @Override
  public void onBrowserEvent(Event event) {
    delegate.onBrowserEvent(event);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    delegate.fireEvent(event);
  }

  @Override
  public void addStyleName(String styleName) {
    delegate.addStyleName(styleName);
  }

  @Override
  public void removeStyleName(String styleName) {
    delegate.removeStyleName(styleName);
  }

  @Override
  public String getStyleName() {
    return delegate.getStyleName();
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return delegate.addAttachHandler(handler);
  }

  @Override
  public boolean isAttached() {
    return delegate.isAttached();
  }

}
