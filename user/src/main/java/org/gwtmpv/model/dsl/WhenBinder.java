package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import org.gwtmpv.model.properties.Property;

/** Does various things as the boolean property changes from true/false. */
public class WhenBinder<P> {

  private final Property<P> property;

  public WhenBinder(final Property<P> property) {
    this.property = property;
  }

  public WhenIsBinder<P> is(final P value) {
    return new WhenIsBinder<P>(property, new WhenCondition<P>() {
      public boolean evaluate(Property<P> property) {
        return eq(property.get(), value);
      }
    });
  }

  @SuppressWarnings("unchecked")
  public WhenIsBinder<P> is(WhenCondition<? super P> condition) {
    // any condition written for a superclass of P is fine
    return new WhenIsBinder<P>(property, (WhenCondition<P>) condition);
  }

}
