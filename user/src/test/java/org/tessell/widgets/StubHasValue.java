package org.tessell.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;
import com.google.gwt.user.client.ui.HasValue;

public class StubHasValue<T> implements HasValue<T> {

  private final EventBus handlers = new SimplerEventBus();
  private T value;

  public StubHasValue() {
  }

  public StubHasValue(T initialValue) {
    this.value = initialValue;
  }

  // By default we should fire events, so encourage tests to call this one
  public void set(final T value) {
    this.setValue(value, true);
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void setValue(final T value) {
    this.value = value;
  }

  @Override
  public void setValue(final T value, final boolean fireEvents) {
    this.value = value;
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

}
