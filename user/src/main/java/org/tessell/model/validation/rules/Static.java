package org.tessell.model.validation.rules;

import org.tessell.model.validation.Valid;

/** A rule that you can explicitly set to valid or invalid as needed. */
public class Static extends AbstractRule<Object> {

  private boolean valid = true;

  public Static(final String message) {
    super(message);
  }

  @Override
  protected Valid isValid() {
    return valid ? Valid.YES : Valid.NO;
  }

  public void set(final boolean valid) {
    this.valid = valid;
    if (property != null) {
      property.reassess();
    }
  }

  public void setMessage(final String message) {
    this.message = message;
  }

}
