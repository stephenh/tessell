package org.tessell.gwt.user.client.ui;

import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

@SuppressWarnings("deprecation")
public class StubTextBoxBase extends StubValueBoxBase<String> implements IsTextBoxBase {

  public StubTextBoxBase() {
    super("");
  }

  public void type(final String value) {
    setValue(value, true);
    blur();
  }

  public void typeEach(String value) {
    for (char c : value.toCharArray()) {
      press(c);
    }
    // now fire the change + blur event
    type(value);
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

}
