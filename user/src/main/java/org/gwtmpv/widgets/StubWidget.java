package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class StubWidget implements IsWidget {

  protected final EventBus handlers = new SimplerEventBus();
  private final StubElement element = new StubElement();
  private boolean attached = false;
  public int absoluteTop;
  public int absoluteLeft;
  public int offsetWidth;
  public int offsetHeight;
  private String debugId;

  public StubWidget() {
    element.setWidget(this);
  }

  public void fireAttached() {
    attached = true;
    AttachEvent.fire(this, true);
  }

  public void fireDetached() {
    attached = false;
    AttachEvent.fire(this, false);
  }

  @Override
  public void onBrowserEvent(final Event event) {
    throw new UnsupportedOperationException("This is a stub.");
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

  @Override
  public void addStyleName(final String style) {
    element.addStyleName(style);
  }

  @Override
  public void removeStyleName(final String style) {
    element.removeStyleName(style);
  }

  @Override
  public int getAbsoluteTop() {
    return absoluteTop;
  }

  @Override
  public int getAbsoluteLeft() {
    return absoluteLeft;
  }

  @Override
  public StubStyle getStyle() {
    return element.getStyle();
  }

  @Override
  public int getOffsetWidth() {
    return offsetWidth;
  }

  @Override
  public int getOffsetHeight() {
    return offsetHeight;
  }

  @Override
  public void ensureDebugId(final String debugId) {
    this.debugId = debugId;
    onEnsureDebugId(debugId);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + debugId + "]";
  }

  @Override
  public String getStyleName() {
    return element.getStyleName();
  }

  @Override
  public Widget asWidget() {
    throw new UnsupportedOperationException("This is a stub");
  }

  @Override
  public StubElement getIsElement() {
    return element;
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return handlers.addHandler(AttachEvent.getType(), handler);
  }

  @Override
  public boolean isAttached() {
    return attached;
  }

  // for subclasses to override
  protected void onEnsureDebugId(String baseDebugId) {
  }

}
