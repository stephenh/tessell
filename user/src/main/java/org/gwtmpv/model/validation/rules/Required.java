package org.gwtmpv.model.validation.rules;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;

/** Validates that a property is not-null. */
public class Required extends AbstractRule<Object, Required> {

  @SuppressWarnings("unchecked")
  public Required(final Property<? extends Object> property, final String message) {
    super((Property<Object>) property, message);
  }

  @SuppressWarnings("unchecked")
  public Required(final Property<? extends Object> property) {
    super((Property<Object>) property, property.getName() + " is required");
  }

  protected Valid isValid() {
    final Object value = property.get();
    if (value instanceof String) {
      // hack to treat empty strings as not entered
      return value != null && ((String) value).length() > 0 ? Valid.YES : Valid.NO;
    } else {
      return value != null ? Valid.YES : Valid.NO;
    }
  }

  @Override
  protected Required getThis() {
    return this;
  }

}