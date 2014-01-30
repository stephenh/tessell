package org.tessell.model.validation.rules;


/** Validates that an int in range. */
public class Range extends AbstractRule<Integer> {

  private final Integer min;
  private final Integer max;

  public Range(final String message, final Integer min, final Integer max) {
    super(message);
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValid() {
    final Integer value = property.get();
    if (value == null) {
      return false;
    }
    if (min != null && value < min) {
      return false;
    }
    if (max != null && value > max) {
      return false;
    }
    return true;
  }

}
