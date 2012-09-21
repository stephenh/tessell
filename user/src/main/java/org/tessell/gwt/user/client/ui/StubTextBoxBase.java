package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

@SuppressWarnings("deprecation")
public class StubTextBoxBase extends StubValueBoxBase<String> implements IsTextBoxBase {

  public StubTextBoxBase() {
    super("");
  }

  /** Simulates the user typing {@code} all at once, e.g. a single change event. */
  public void type(final String value) {
    setValue(value, true);
    blur();
  }

  /** Simulates the user typing {@code value}, with a key down/press/up for each char, then a final change. */
  public void typeEach(String value) {
    String oldValue = getValue();
    for (char c : value.toCharArray()) {
      press(c);
    }
    // can't use type because our value is already the same,
    // since press(c) has iteratively updated it
    ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    blur();
  }

  @Override
  public void press(char c) {
    keyDown(c);
    setValue(getValue() + c, false);
    keyPress(c);
    keyUp(c);
  }

  @Override
  public void setValue(final String value, final boolean fireEvents) {
    super.setValue(value == null ? "" : value, fireEvents);
  }

  @Override
  public void setTextAlignment(final TextAlignConstant align) {
  }

  @Override
  public void setText(String value) {
    setValue(value);
  }

  @Override
  public String getText() {
    return getValue();
  }

  @Override
  public TextBoxBase asWidget() {
    throw new UnsupportedOperationException("This is a stub");
  }

}
