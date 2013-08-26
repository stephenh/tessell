package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.StubKeyModifiers;

import com.google.gwt.event.dom.client.KeyCodes;
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
    press(value);
  }

  @Override
  public void keyUp(int keyCode, StubKeyModifiers mods) {
    if (keyCode == KeyCodes.KEY_BACKSPACE) {
      // we don't keep track of cursor position, so delete at the end
      if (getValue().length() > 0) {
        setValue(getValue().substring(0, getValue().length() - 1));
      }
    } else if (keyCode == KeyCodes.KEY_DELETE) {
      // we don't keep track of cursor position, so delete at the beginning
      if (getValue().length() > 0) {
        setValue(getValue().substring(1, getValue().length()));
      }
    }
    super.keyUp(keyCode, mods);
  }

  @Override
  public void keyPress(char c, StubKeyModifiers mods) {
    setValue(getValue() + c, false);
    super.keyPress(c, mods);
  }

  @Override
  public void press(int keyCode) {
    String oldValue = getValue();
    super.press(keyCode);
    ValueChangeEvent.fireIfNotEqual(this, oldValue, getValue());
    blur();
  }

  @Override
  public void press(char charCode) {
    String oldValue = getValue();
    super.press(charCode);
    ValueChangeEvent.fireIfNotEqual(this, oldValue, getValue());
    blur();
  }

  @Override
  public void press(String charCodes) {
    String oldValue = getValue();
    super.press(charCodes);
    ValueChangeEvent.fireIfNotEqual(this, oldValue, getValue());
    blur();
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
