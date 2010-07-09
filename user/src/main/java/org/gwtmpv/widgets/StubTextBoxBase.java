package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

public class StubTextBoxBase extends StubFocusWidget implements IsTextBoxBase {

  private String value = "";

  public void type(final String value) {
    setValue(value, true);
    blur();
  }

  public void press(final char c) {
    keyDown();
    this.value = this.value + c;
    keyPress();
    keyUp();
  }

  @Override
  public void setValue(final String value, final boolean fireEvents) {
    final String oldValue = this.value;
    this.value = value == null ? "" : value;
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(final String value) {
    setValue(value, false);
  }

  @Override
  public String getSelectedText() {
    return null;
  }

  @Override
  public int getSelectionLength() {
    return 0;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public void setCursorPos(final int pos) {

  }

  @Override
  public void setReadOnly(final boolean readOnly) {

  }

  @Override
  public void setSelectionRange(final int pos, final int length) {

  }

  @Override
  public void setTextAlignment(final TextAlignConstant align) {

  }

  @Override
  public HandlerRegistration addChangeHandler(final ChangeHandler handler) {
    return null;
  }

  @Override
  public String getText() {
    return getValue();
  }

  @Override
  public void setText(final String text) {
    setValue(text);
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public void setName(final String name) {
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

}
