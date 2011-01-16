package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for making logical {@link IsWidget}s that can be unit tested.
 *
 * E.g.:
 *
 * <code>
 *     class MyWidget extends CompositeIsWidget {
 *       private final IsTextBox textBox = newTextBox();
 *       
 *       public MyWidget() {
 *         setWidget(textBox);
 *       }
 *       
 *       public void customLogic() {
 *         textBox.getStyle().setBackgroundColor("blue");
 *       }
 *     }
 * </code>
 *
 * Note that only widgets that do not rely directly on the DOM
 * can be built in this fashion. E.g. {@link #onBrowserEvent(Event)}
 * is not implemented for stub/test widgets.
 *
 * If you do have a widget that needs direct DOM access, you'll
 * have to make your own {@code IsMyWidget}, {@code GwtMyWidget},
 * and {@code StubMyWidget} classes, so that {@code GwtMyWidget}
 * can do whatever it wants with DOM/browser-coupled code, and
 * {@code StubMyWidget} will pretend to do it for tests.
 */
public class CompositeIsWidget implements IsWidget {

  protected IsWidget widget;

  protected void setWidget(final IsWidget widget) {
    this.widget = widget;
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      throw new IllegalStateException("CompositeIsWidget.setWidget was not called");
    }
    return widget.asWidget();
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
    return widget.getIsElement();
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return widget.addAttachHandler(handler);
  }

  @Override
  public boolean isAttached() {
    return widget.isAttached();
  }

}
