package org.tessell.model.validation.rules;

import java.util.List;

import org.tessell.model.validation.Valid;

public class Size<E> extends AbstractRule<List<E>> {

  private final Integer min;
  private final Integer max;

  public Size(String message, Integer min, Integer max) {
    super(message);
    this.min = min;
    this.max = max;
  }

  @Override
  protected Valid isValid() {
    final Integer value = property.get().size();
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
