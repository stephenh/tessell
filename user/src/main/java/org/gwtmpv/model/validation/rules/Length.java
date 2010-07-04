package org.gwtmpv.model.validation.rules;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;

/** Validates that a string. */
public class Length extends AbstractRule<String, Length> {

  private final int min;
  private final int max;

  public Length(final Property<String> property, final String message) {
    this(property, message, 1, 100);
  }

  public Length(final Property<String> property, final String message, final int min, final int max) {
    super(property, message);
    this.min = min;
    this.max = max;
  }

  @Override
  protected Valid isValid() {
    final String value = property.get();
    if (value == null) {
      return Valid.NO;
    }
    return value.length() >= min && value.length() <= max ? Valid.YES : Valid.NO;
  }

  @Override
  protected Length getThis() {
    return this;
  }

}