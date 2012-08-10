package org.tessell.model.dsl;

import com.google.gwt.user.client.TakesValue;

public class SetPropertyBinder<P> {

  private final TakesValue<P> value;
  private final Setup setup;

  // Allows "set" functionality to be bound to multiple sources
  static interface Setup {
    HandlerRegistrations setup(final Runnable runnable);
  }

  SetPropertyBinder(final TakesValue<P> value, final Setup setup) {
    this.value = value;
    this.setup = setup;
  }

  public HandlerRegistrations to(final P newValue) {
    return setup.setup(new Runnable() {
      public void run() {
        value.setValue(newValue);
      }
    });
  }

}
