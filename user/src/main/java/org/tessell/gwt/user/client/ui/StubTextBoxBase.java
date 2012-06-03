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

  public void press(final char... chars) {
    for (char c : chars) {
      keyDown();
      setValue(getValue() + c, false);
      keyPress();
      keyUp();
    }
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
