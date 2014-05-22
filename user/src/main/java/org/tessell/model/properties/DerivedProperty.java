package org.tessell.model.properties;

import org.tessell.model.values.DerivedValue;

/**
 * Creates a derived property where the subclass's {@link #getDerivedValue()} can
 * return a calculated value and we will automatically update anytime values
 * used in the calculation change.
 *
 * E.g.:
 * 
 * <code>
 * DerivedProperty<String> withSuffix = new DerivedProperty<String>() {
 *   protected String getDerivedValue() {
 *     return otherProperty.get() + " suffix";
 *   }
 * };
 * </code>
 */
public abstract class DerivedProperty<P> extends AbstractProperty<P, DerivedProperty<P>> {

  public DerivedProperty() {
    initializeValue(new DerivedValue<P>() {
      public P get() {
        return DerivedProperty.this.getDerivedValue();
      }
    });
  }

  protected abstract P getDerivedValue();

  @Override
  protected DerivedProperty<P> getThis() {
    return this;
  }

}
