package org.gwtmpv.model.properties;

import org.gwtmpv.model.values.Value;

/** A subclass of {@link AbstractProperty} without any extra features for quick variable-typed properties. */
public class BasicProperty<P> extends AbstractProperty<P, BasicProperty<P>> {

  public BasicProperty(final Value<P> value) {
    super(value);
  }

  @Override
  protected BasicProperty<P> getThis() {
    return this;
  }

}
