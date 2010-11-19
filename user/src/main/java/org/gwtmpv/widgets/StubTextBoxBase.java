package org.gwtmpv.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

@SuppressWarnings("deprecation")
public class StubTextBoxBase extends StubValueBoxBase<String> implements IsTextBoxBase {

  private String value = "";

  public void type(final String value) {
    setValue(value, true);
    blur();
  }

  public void press(final char c) {
    keyDown();
    value = value + c;
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
  public void setTextAlignment(final TextAlignConstant align) {
  }

}
