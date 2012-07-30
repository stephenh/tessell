package org.tessell.model.dsl;

import static java.lang.Boolean.TRUE;

import org.tessell.gwt.user.client.ui.IsRadioButton;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.BooleanProperty;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/** Binds BooleanProperties to widgets. */
public class BooleanPropertyBinder extends PropertyBinder<Boolean> {

  private final BooleanProperty bp;

  public BooleanPropertyBinder(BooleanProperty bp) {
    super(bp);
    this.bp = bp;
  }

  public HandlerRegistrations to(final IsRadioButton ifTrue, final IsRadioButton ifFalse) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(ifTrue.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (TRUE.equals(event.getValue())) {
          bp.set(true);
          set(ifTrue, ifFalse); // update ifFalse
        }
      }
    }));
    hr.add(ifFalse.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (TRUE.equals(event.getValue())) {
          bp.set(false);
          set(ifFalse, ifTrue); // update ifTrue
        }
      }
    }));
    hr.add(bp.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        if (bp.isTrue()) {
          set(ifTrue, ifFalse);
        } else {
          set(ifFalse, ifTrue);
        }
      }
    }));
    // set the initial value
    if (bp.isTrue()) {
      set(ifTrue, ifFalse);
    } else {
      set(ifFalse, ifTrue);
    }
    return hr;
  }

  private void set(IsRadioButton nowChecked, IsRadioButton notChecked) {
    nowChecked.setValue(true);
    // the browser doesn't do this, so don't fire an event, but keep our stubs up to date
    notChecked.setValue(false, false);
  }
}
