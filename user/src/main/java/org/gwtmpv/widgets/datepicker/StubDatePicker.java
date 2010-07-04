package org.gwtmpv.widgets.datepicker;

import java.util.Date;

import org.gwtmpv.widgets.StubWidget;

import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubDatePicker extends StubWidget implements IsDatePicker {

  private Date value;

  @Override
  public HandlerRegistration addHighlightHandler(final HighlightHandler<Date> handler) {
    return handlers.addHandler(HighlightEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addShowRangeHandler(final ShowRangeHandler<Date> handler) {
    return handlers.addHandler(ShowRangeEvent.getType(), handler);
  }

  @Override
  public Date getValue() {
    return value;
  }

  @Override
  public void setValue(final Date value) {
    this.value = value;
  }

  @Override
  public void setValue(final Date value, final boolean fireEvents) {
    this.value = value;
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

}
