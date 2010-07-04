package org.gwtmpv.widgets.datepicker;

import java.util.Date;

import org.gwtmpv.widgets.IsWidget;

import com.google.gwt.event.logical.shared.HasHighlightHandlers;
import com.google.gwt.event.logical.shared.HasShowRangeHandlers;
import com.google.gwt.user.client.ui.HasValue;

public interface IsDatePicker extends IsWidget, HasHighlightHandlers<Date>, HasShowRangeHandlers<Date>, HasValue<Date> {
}
