package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class GwtRadioButton implements IsRadioButton {

  private final RadioButton button;

  public GwtRadioButton(final RadioButton button) {
    this.button = button;
  }

  @Override
  public int getTabIndex() {
    return button.getTabIndex();
  }

  @Override
  public boolean isEnabled() {
    return button.isEnabled();
  }

  @Override
  public void setAccessKey(final char key) {
    button.setAccessKey(key);
  }

  @Override
  public void setEnabled(final boolean enabled) {
    button.setEnabled(enabled);
  }

  @Override
  public void setFocus(final boolean focused) {
    button.setFocus(focused);
  }

  @Override
  public void setTabIndex(final int index) {
    button.setTabIndex(index);
  }

  @Override
  public int getAbsoluteLeft() {
    return button.getAbsoluteLeft();
  }

  @Override
  public int getAbsoluteTop() {
    return button.getAbsoluteTop();
  }

  @Override
  public void onBrowserEvent(final Event event) {
    button.onBrowserEvent(event);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    button.fireEvent(event);
  }

  @Override
  public void addStyleName(final String styleName) {
    button.addStyleName(styleName);
  }

  @Override
  public void removeStyleName(final String styleName) {
    button.removeStyleName(styleName);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return button.addClickHandler(handler);
  }

  @Override
  public HandlerRegistration addFocusHandler(final FocusHandler handler) {
    return button.addFocusHandler(handler);
  }

  @Override
  public HandlerRegistration addBlurHandler(final BlurHandler handler) {
    return button.addBlurHandler(handler);
  }

  @Override
  public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler) {
    return button.addKeyUpHandler(handler);
  }

  @Override
  public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler) {
    return button.addKeyDownHandler(handler);
  }

  @Override
  public HandlerRegistration addKeyPressHandler(final KeyPressHandler handler) {
    return button.addKeyPressHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
    return button.addMouseDownHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
    return button.addMouseUpHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
    return button.addMouseOutHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
    return button.addMouseOverHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
    return button.addMouseMoveHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
    return button.addMouseWheelHandler(handler);
  }

  @Override
  public String getHTML() {
    return button.getHTML();
  }

  @Override
  public void setHTML(final String html) {
    button.setHTML(html);
  }

  @Override
  public String getText() {
    return button.getText();
  }

  @Override
  public void setText(final String text) {
    button.setText(text);
  }

  @Override
  public String getName() {
    return button.getName();
  }

  @Override
  public void setName(final String name) {
    button.setName(name);
  }

  @Override
  public Boolean getValue() {
    return button.getValue();
  }

  @Override
  public void setValue(final Boolean value) {
    button.setValue(value);
  }

  @Override
  public void setValue(final Boolean value, final boolean fireEvents) {
    button.setValue(value, fireEvents);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> handler) {
    return button.addValueChangeHandler(handler);
  }

  @Override
  public int getOffsetHeight() {
    return button.getOffsetHeight();
  }

  @Override
  public int getOffsetWidth() {
    return button.getOffsetWidth();
  }

  @Override
  public void ensureDebugId(final String id) {
    button.ensureDebugId(id);
  }

  @Override
  public String getStyleName() {
    return button.getStyleName();
  }

  @Override
  public Widget asWidget() {
    return button;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(button.getElement());
  }

  @Override
  public HandlerRegistration addAttachHandler(Handler handler) {
    return button.addAttachHandler(handler);
  }

  @Override
  public boolean isAttached() {
    return button.isAttached();
  }

}
