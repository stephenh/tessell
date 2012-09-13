package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubSimpleCheckBox extends StubFocusWidget implements IsSimpleCheckBox {

  private String name;
  private String formValue;
  private Boolean value;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setValue(Boolean value) {
    this.value = value;
  }

  @Override
  public Boolean getValue() {
    return value;
  }

  @Override
  public String getFormValue() {
    return formValue;
  }

  @Override
  public void setFormValue(String formValue) {
    this.formValue = formValue;
  }

  @Override
  public void setValue(Boolean value, boolean fireEvents) {
    Boolean oldValue = getValue();
    this.value = value;
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

}
