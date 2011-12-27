package org.tessell.model.validation.rules;

import static java.lang.Boolean.TRUE;

import org.bindgen.Binding;
import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;
import org.tessell.model.values.BoundValue;
import org.tessell.model.values.Value;

/** A rule for applying custom logic. */
public class Custom extends AbstractRule<Object, Custom> {

  private final Value<Boolean> value;

  /** Adds a rule that will trigger against {@code property} whenever {@code property} itself is false. */
  public Custom(final Property<Boolean> property, final String message) {
    this(property, message, property);
  }

  /** Adds a rule that will trigger against {@code property} whenever {@code value} is false. */
  public Custom(final Property<?> property, final String message, final Binding<Boolean> value) {
    this(property, message, new BoundValue<Boolean>(value));
  }

  @SuppressWarnings("unchecked")
  public Custom(final Property<?> property, final String message, final Value<Boolean> value) {
    super((Property<Object>) property, message);
    this.value = value;
  }

  @Override
  protected Valid isValid() {
    return TRUE.equals(value.get()) ? Valid.YES : Valid.NO;
  }

  @Override
  protected Custom getThis() {
    return this;
  }

}
