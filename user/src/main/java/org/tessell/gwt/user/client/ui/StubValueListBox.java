package org.tessell.gwt.user.client.ui;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubValueListBox extends StubListBox implements IsValueListBox {

  public void select(final String text) {
    for (int i = 0; i < getItemCount(); i++) {
      if (getItemText(i).equals(text)) {
        setValue(getValue(i), true);
        blur();
        return;
      }
    }
    throw new IllegalStateException("No item with text " + text);
  }

  @Override
  public String getValue() {
    if (getSelectedIndex() == -1) {
      return null;
    }
    return getValue(getSelectedIndex());
  }

  @Override
  public void setValue(final String value) {
    for (int i = 0; i < getItemCount(); i++) {
      if (getValue(i).equals(value) || (value == null && "".equals(getValue(i)))) {
        setSelectedIndex(i);
        return;
      }
    }
    if (value == null) {
      setSelectedIndex(-1);
    } else {
      throw new IllegalStateException("No item with value " + value);
    }
  }

  @Override
  public void setValue(final String value, final boolean fireEvents) {
    setValue(value);
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

}
