package org.tessell.model.validation.rules;

import org.tessell.model.validation.Valid;
import org.tessell.util.Supplier;

/** A rule for applying custom logic. */
public class Custom extends AbstractRule<Object> {

  private final Supplier<Boolean> value;

  /** Adds a rule that will trigger {@code value} itself is false. */
  public Custom(final String message, final Supplier<Boolean> value) {
    super(message);
    this.value = value;
  }

  @Override
  protected Valid isValid() {
    return Valid.fromBoolean(value.get());
  }
}
