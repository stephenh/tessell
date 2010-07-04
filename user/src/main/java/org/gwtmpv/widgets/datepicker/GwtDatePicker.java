package org.gwtmpv.widgets.datepicker;

import org.gwtmpv.widgets.GwtElement;
import org.gwtmpv.widgets.IsElement;
import org.gwtmpv.widgets.IsStyle;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class GwtDatePicker extends DatePicker implements IsDatePicker {

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

}
