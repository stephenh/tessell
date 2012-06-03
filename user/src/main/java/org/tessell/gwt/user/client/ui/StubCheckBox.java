package org.tessell.gwt.user.client.ui;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubCheckBox extends StubButtonBase implements IsCheckBox {

  private String name;
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

  @Override
  public void click() {
    final Boolean newValue = (value == null) ? true : !value;
    setValue(newValue, true);
    super.click();
  }

  public void set(final Boolean value) {
    setValue(value, true);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public Boolean getValue() {
    return value;
  }

  @Override
  public void setValue(final Boolean value) {
    this.value = value;
  }

  @Override
  public void setValue(final Boolean value, final boolean fireEvents) {
    final Boolean oldValue = this.value;
    this.value = value;
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

}
