package org.tessell.gwt.user.client.ui;

import static java.lang.Boolean.TRUE;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubSimpleCheckBox extends StubFocusWidget implements IsSimpleCheckBox {

  private String name;
  private String formValue;
  private Boolean value;

  public void check() {
    if (value != null && value.booleanValue()) {
      throw new RuntimeException(name + " is already checked");
    }
    click();
  }

  public void uncheck() {
    if (value == null || !value.booleanValue()) {
      throw new RuntimeException(name + " is not checked");
    }
    click();
  }

  public boolean isChecked() {
    return TRUE.equals(getValue());
  }

  @Override
  public void click() {
    final Boolean newValue = (value == null) ? true : !value;
    setValue(newValue, true);
    super.click();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setValue(Boolean value) {
    setValue(value, true);
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
