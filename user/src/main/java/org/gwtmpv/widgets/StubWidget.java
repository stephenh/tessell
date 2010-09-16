package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class StubWidget implements IsWidget, HasStubCss {

  protected final HandlerManager handlers = new HandlerManager(this);
  private final StubIsElement element = new StubIsElement();
  public int absoluteTop;
  public int absoluteLeft;
  public int offsetWidth;
  public int offsetHeight;
  private String debugId;

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
  public List<String> getStyleNames() {
    return element.getStyleNames();
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
  public IsElement getIsElement() {
    return element;
  }

}
