package org.gwtmpv.model.properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.Value;

public class BooleanProperty extends AbstractProperty<Boolean, BooleanProperty> {

  public BooleanProperty(final Value<Boolean> value) {
    super(value);
  }

  @Override
  protected BooleanProperty getThis() {
    return this;
  }

  public void toggle() {
    set(isTrue() ? false : true);
  }

  public boolean isTrue() {
    return TRUE.equals(get());
  }

  public BooleanProperty not() {
    final BooleanProperty parent = this;
    return addDerived(new BooleanProperty(new DerivedValue<Boolean>() {
      public Boolean get() {
        return FALSE.equals(parent.get());
      }
    }));
  }
}
