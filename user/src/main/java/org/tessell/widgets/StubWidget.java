package org.tessell.widgets;

import java.util.Iterator;

import org.tessell.gwt.dom.client.StubElement;
import org.tessell.gwt.dom.client.StubStyle;
import org.tessell.gwt.user.client.ui.IsWidget;

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
        // this is kind of odd, but given tests the change to grab
        // the CompositeIsWidget and not just it's wrapped element
        stub = (StubWidget) ((CompositeIsWidget) next).widget;
        if (id.equals(stub.getIsElement().getId())) {
          return next;
        }
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
