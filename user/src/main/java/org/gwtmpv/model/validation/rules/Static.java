package org.gwtmpv.model.validation.rules;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;

/** A rule for applying custom logic. */
public class Static extends AbstractRule<Object, Static> {

  private boolean valid;

  @SuppressWarnings("unchecked")
  public Static(final Property<?> property, final String message) {
    super((Property<Object>) property, message);
  }

  @Override
  protected Valid isValid() {
    return valid ? Valid.YES : Valid.NO;
  }

  @Override
  protected Static getThis() {
    return this;
  }

  public void set(final boolean valid) {
    this.valid = valid;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

}