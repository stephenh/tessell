package org.tessell.widgets;

import java.util.Iterator;

import org.tessell.gwt.dom.client.StubElement;
import org.tessell.gwt.dom.client.StubStyle;
import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class StubWidget implements IsWidget, IsStubWidget {

  protected final EventBus handlers = new SimplerEventBus();
  private final StubElement element = new StubElement();
  private boolean attached = false;
  public int absoluteTop;
  public int absoluteLeft;
  public int offsetWidth;
  public int offsetHeight;

  public StubWidget() {
    element.setWidget(this);
  }

  /** @return the widget for {@code id} or {@code null} */
  public IsWidget findById(String id) {
    if (element.getId() != null && element.getId().equals(id)) {
      return this;
    }
    return findInChildren(id);
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
    element.setId(debugId);
    onEnsureDebugId(debugId);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + element.getId() + "]";
  }

  @Override
  public String getStyleName() {
    return element.getStyleName();
  }

  @Override
  public void setStyleName(String styleName) {
    element.setStyleName(styleName);
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

  @Override
  public <H extends EventHandler> HandlerRegistration addDomHandler(final H handler, DomEvent.Type<H> type) {
    return handlers.addHandler(type, handler);
  }

  // for subclasses to override
  protected void onEnsureDebugId(String baseDebugId) {
  }

  // for subclasses that contain children to override
  protected IsWidget findInChildren(String id) {
    return null;
  }

  /** Looks recursively into {@code widgets} for one with {@code id}. */
  protected static IsWidget findInChildren(Iterator<IsWidget> widgets, String id) {
    while (widgets.hasNext()) {
      IsWidget next = widgets.next();
      final StubWidget stub;
      if (next instanceof CompositeIsWidget) {
        // assume the CompositeIsWidget is wrapping a stub widget
        stub = (StubWidget) ((CompositeIsWidget) next).getIsWidget();
      } else {
        stub = (StubWidget) next;
      }
      IsWidget found = stub.findById(id);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
}
