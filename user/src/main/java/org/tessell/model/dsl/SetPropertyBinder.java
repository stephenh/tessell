package org.tessell.model.dsl;

import org.tessell.model.properties.Property;

public class SetPropertyBinder<P> {

  private final Property<P> property;
  private final Setup setup;

  // Allows "set" functionality to be bound to multiple sources
  static interface Setup {
    HandlerRegistrations setup(Runnable runnable);
  }

  SetPropertyBinder(Property<P> property, Setup setup) {
    this.property = property;
    this.setup = setup;
  }

  public HandlerRegistrations to(final P newValue) {
    return setup.setup(new Runnable() {
      public void run() {
        property.set(newValue);
      }
    });
  }

}
