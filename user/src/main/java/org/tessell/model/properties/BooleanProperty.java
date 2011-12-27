package org.tessell.model.properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;

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
