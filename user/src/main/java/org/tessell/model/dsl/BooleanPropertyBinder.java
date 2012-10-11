package org.tessell.model.dsl;

import static java.lang.Boolean.TRUE;

import java.util.List;

import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.gwt.user.client.ui.IsRadioButton;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.ListProperty;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

/** Binds BooleanProperties to widgets. */
public class BooleanPropertyBinder extends PropertyBinder<Boolean> {

  private final BooleanProperty bp;

  public BooleanPropertyBinder(final Binder b, final BooleanProperty bp) {
    super(b, bp);
    this.bp = bp;
  }

  /** Bind our property to source, two-way, and shows view when our property is true. */
  public void to(final HasValue<Boolean> source, HasCss view) {
    super.to(source);
    b.when(bp).is(true).show(view);
  }

  public <V> BooleanPropertyToListBinder<V> to(final List<V> values) {
    return new BooleanPropertyToListBinder<V>(b, bp, values);
  }

  public <V> BooleanPropertyToListPropertyBinder<V> to(final ListProperty<V> values) {
    return new BooleanPropertyToListPropertyBinder<V>(b, bp, values);
  }

  public void to(final IsRadioButton ifTrue, final IsRadioButton ifFalse) {
    b.add(ifTrue.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (TRUE.equals(event.getValue())) {
          bp.set(true);
          set(ifTrue, ifFalse); // update ifFalse
        }
      }
    }));
    b.add(ifFalse.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (TRUE.equals(event.getValue())) {
          bp.set(false);
          set(ifFalse, ifTrue); // update ifTrue
        }
      }
    }));
    b.add(bp.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
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
  }

  private void set(IsRadioButton nowChecked, IsRadioButton notChecked) {
    nowChecked.setValue(true);
    // the browser doesn't do this, so don't fire an event, but keep our stubs up to date
    notChecked.setValue(false, false);
  }
}
