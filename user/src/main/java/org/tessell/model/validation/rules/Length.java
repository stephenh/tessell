package org.tessell.model.validation.rules;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;

/** Validates that a string. */
public class Length extends AbstractRule<String, Length> {

  private final Property<String> property;
  private final int min;
  private final int max;

  public Length(final Property<String> property, final String message) {
    this(property, message, 1, 100);
  }

  public Length(final Property<String> property, final String message, final int min, final int max) {
    super(property, message);
    this.min = min;
    this.max = max;
    this.property = property;
  }

  @Override
  protected Valid isValid() {
    final String value = property.get();
    if (value == null) {
      return Valid.YES; // defer to Required, if it's around
    }
    return value.length() >= min && value.length() <= max ? Valid.YES : Valid.NO;
  }

}
