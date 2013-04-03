package org.tessell.model.properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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

  /** @return whether this property is true (null-safe). */
  public boolean isTrue() {
    return TRUE.equals(get());
  }

  /** @return whether this property is false (null-safe). */
  public boolean isFalse() {
    return FALSE.equals(get());
  }

  /** @return the inverse of this property. */
  public Property<Boolean> not() {
    final BooleanProperty parent = this;
    return formatted(new PropertyFormatter<Boolean, Boolean>() {
      public Boolean format(Boolean a) {
        return !parent.get();
      }

      public Boolean parse(Boolean b) throws Exception {
        return !b;
      }
    });
  }
}
