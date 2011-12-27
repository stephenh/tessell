package org.tessell.widgets.datepicker;

import java.util.Date;

import org.tessell.widgets.IsWidget;

import com.google.gwt.event.logical.shared.HasHighlightHandlers;
import com.google.gwt.event.logical.shared.HasShowRangeHandlers;
import com.google.gwt.user.client.ui.HasValue;

public interface IsDatePicker extends IsWidget, HasHighlightHandlers<Date>, HasShowRangeHandlers<Date>, HasValue<Date> {
}
