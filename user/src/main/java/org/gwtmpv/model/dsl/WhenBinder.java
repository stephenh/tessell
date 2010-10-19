package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

/** Does various things as the boolean property changes from true/false. */
public class WhenBinder<P> {

  private final Binder binder;
  private final Property<P> property;

  public WhenBinder(final Binder binder, final Property<P> property) {
    this.binder = binder;
    this.property = property;
  }

  public WhenIsBinder<P> is(P value) {
    return new WhenIsBinder<P>(binder, property, value);
  }

}
