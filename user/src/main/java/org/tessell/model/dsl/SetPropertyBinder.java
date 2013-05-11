package org.tessell.model.dsl;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;

public class SetPropertyBinder<P> {

  private final Binder b;
  private final TakesValue<P> value;
  private final Setup setup;

  // Allows "set" functionality to be bound to multiple sources
  static interface Setup {
    HandlerRegistration setup(final Runnable runnable);
  }

  SetPropertyBinder(final Binder b, final TakesValue<P> value, final Setup setup) {
    this.b = b;
    this.value = value;
    this.setup = setup;
  }

  public void to(final P newValue) {
    b.add(setup.setup(new Runnable() {
      public void run() {
        value.setValue(newValue);
      }
    }));
  }

  public void to(final HasValue<P> hasValue) {
    b.add(setup.setup(new Runnable() {
      public void run() {
        value.setValue(hasValue.getValue());
      }
    }));
  }

}
