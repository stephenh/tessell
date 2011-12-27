package org.tessell.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ValueListBox extends GwtListBox implements IsValueListBox {

  private boolean valueChangeHandlerInitialized;

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
    if (!valueChangeHandlerInitialized) {
      valueChangeHandlerInitialized = true;
      addChangeHandler(new ChangeHandler() {
        public void onChange(final ChangeEvent event) {
          ValueChangeEvent.fire(ValueListBox.this, getValue());
        }
      });
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

}
