package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

/** Does various things as the boolean property changes from true/false. */
public class WhenBinder<P> {

  private final Property<P> property;

  public WhenBinder(final Property<P> property) {
    this.property = property;
  }

  public WhenIsBinder<P> is(P value) {
    return new WhenIsBinder<P>(property, value);
  }

}
