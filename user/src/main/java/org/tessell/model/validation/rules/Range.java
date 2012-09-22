package org.tessell.model.validation.rules;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;

/** Validates that an int in range. */
public class Range extends AbstractRule<Integer, Range> {

  private final Property<Integer> property;
  private final Integer min;
  private final Integer max;

  public Range(final Property<Integer> property, final String message, final Integer min, final Integer max) {
    super(property, message);
    this.min = min;
    this.max = max;
    this.property = property;
  }

  @Override
  protected Valid isValid() {
    final Integer value = property.get();
    if (value == null) {
      return Valid.NO;
    }
    if (min != null && value < min) {
      return Valid.NO;
    }
    if (max != null && value > max) {
      return Valid.NO;
    }
    return Valid.YES;
  }

}
