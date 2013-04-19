package org.tessell.bootstrap;

import static org.tessell.widgets.Widgets.newHTMLPanel;
import static org.tessell.widgets.Widgets.newInlineLabel;
import static org.tessell.widgets.Widgets.newSimpleRadioButton;

import org.tessell.gwt.user.client.ui.IsHTMLPanel;
import org.tessell.gwt.user.client.ui.IsInlineLabel;
import org.tessell.gwt.user.client.ui.IsRadioButton;
import org.tessell.gwt.user.client.ui.IsSimpleRadioButton;
import org.tessell.widgets.CompositeIsWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;

/** Creates a bizstrap-style radio button where the label contains the button+text. */
@SuppressWarnings("deprecation")
public class RadioButton extends CompositeIsWidget implements IsRadioButton {

  protected final IsHTMLPanel label = setWidget(newHTMLPanel("label", ""));
  protected final IsSimpleRadioButton button;
  protected final IsInlineLabel span = newInlineLabel();

  @UiConstructor
  public RadioButton(final String name) {
    button = newSimpleRadioButton(name);
    // setup <label> button span </label>
    label.addStyleName("radio"); // we don't have BizstrapStyle handy
    label.add(button, label.getIsElement());
    label.add(span, label.getIsElement());
  }

  @Override
  protected void onEnsureDebugId(final String baseId) {
    super.onEnsureDebugId(baseId);
    button.ensureDebugId(baseId + "-input");
  }

  @Override
  public String getText() {
    return span.getText();
  }

  @Override
  public void setText(final String text) {
    span.setText(text);
  }

  @Override
  public String getHTML() {
    return span.getIsElement().getInnerHTML();
  }

  @Override
  public void setHTML(final String html) {
    span.getIsElement().setInnerHTML(html);
  }

  @Override
  public void setHTML(final SafeHtml html) {
    span.getIsElement().setInnerHTML(html.asString());
  }

  @Override
  public boolean isEnabled() {
    return button.isEnabled();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    button.setEnabled(enabled);
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
  public int getTabIndex() {
    return button.getTabIndex();
  }

  @Override
  public void setAccessKey(final char key) {
    button.setAccessKey(key);
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
  public void addFocusListener(final FocusListener listener) {
    button.addFocusListener(listener);
  }

  @Override
  public void removeFocusListener(final FocusListener listener) {
    button.removeFocusListener(listener);
  }

  @Override
  public void addKeyboardListener(final KeyboardListener listener) {
    button.addKeyboardListener(listener);
  }

  @Override
  public void removeKeyboardListener(final KeyboardListener listener) {
    button.removeKeyboardListener(listener);
  }

  @Override
  public void setName(final String name) {
    button.setName(name);
  }

  @Override
  public String getName() {
    return button.getName();
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

}
