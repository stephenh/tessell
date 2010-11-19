package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class StubValueBoxBase<T> extends StubFocusWidget implements IsValueBoxBase<T> {

  private T value;
  private boolean readOnly;

  @Override
  public void setValue(final T value, final boolean fireEvents) {
    final T oldValue = this.value;
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void setValue(final T value) {
    setValue(value, false);
  }

  @Override
  public String getSelectedText() {
    return null;
  }

  @Override
  public int getSelectionLength() {
    return 0;
  }

  @Override
  public boolean isReadOnly() {
    return readOnly;
  }

  @Override
  public void setCursorPos(final int pos) {
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
    this.readOnly = readOnly;
  }

  @Override
  public void setSelectionRange(final int pos, final int length) {
  }

  @Override
  public void setAlignment(final TextAlignment align) {
  }

  @Override
  public HandlerRegistration addChangeHandler(final ChangeHandler handler) {
    return null;
  }

  @Override
  public String getText() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void setText(final String text) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public void setName(final String name) {
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

  @Override
  public DirectionEstimator getDirectionEstimator() {
    return null;
  }

  @Override
  public void setDirectionEstimator(boolean enabled) {
  }

  @Override
  public void setDirectionEstimator(DirectionEstimator directionEstimator) {
  }

}
