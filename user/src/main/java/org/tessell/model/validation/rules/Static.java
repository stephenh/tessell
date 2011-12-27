package org.tessell.model.validation.rules;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;

/** A rule for applying custom logic. */
public class Static extends AbstractRule<Object, Static> {

  private boolean valid = true;
  private final Property<?> property;

  @SuppressWarnings("unchecked")
  public Static(final Property<?> property, final String message) {
    super((Property<Object>) property, message);
    this.property = property;
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
    property.reassess();
  }

  public void setMessage(final String message) {
    this.message = message;
  }

}
