package org.tessell.model.validation.rules;

import static java.lang.Boolean.TRUE;

import org.bindgen.Binding;
import org.tessell.model.validation.Valid;
import org.tessell.model.values.BoundValue;
import org.tessell.util.Supplier;

/** A rule for applying custom logic. */
public class Custom extends AbstractRule<Object> {

  private final Supplier<Boolean> value;

  /** Adds a rule that will trigger whenever {@code value} is false. */
  public Custom(final String message, final Binding<Boolean> value) {
    this(message, new BoundValue<Boolean>(value));
  }

  /** Adds a rule that will trigger {@code value} itself is false. */
  public Custom(final String message, final Supplier<Boolean> value) {
    super(message);
    this.value = value;
  }

  @Override
  protected Valid isValid() {
    return TRUE.equals(value.get()) ? Valid.YES : Valid.NO;
  }

}
