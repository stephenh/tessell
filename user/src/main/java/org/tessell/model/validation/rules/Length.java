package org.tessell.model.validation.rules;

import org.tessell.model.validation.Valid;

/** Validates that a string. */
public class Length extends AbstractRule<String> {

  private final int min;
  private final int max;

  public Length(final String message) {
    this(message, 1, 100);
  }

  public Length(final String message, final int min, final int max) {
    super(message);
    this.min = min;
    this.max = max;
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
