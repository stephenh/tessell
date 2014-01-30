package org.tessell.model.validation.rules;

import java.util.List;

public class Size<E> extends AbstractRule<List<E>> {

  private final Integer min;
  private final Integer max;

  public Size(String message, Integer min, Integer max) {
    super(message);
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValid() {
    final Integer value = property.get().size();
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
