package org.tessell.model.validation.rules;

import org.tessell.model.validation.Valid;

/** A rule that you can explicitly set to valid or invalid as needed. */
public class Static extends AbstractRule<Object> {

  private Valid valid = Valid.TRUE;

  public Static(final String message) {
    super(message);
  }

  // promoted to public access for AbstractRule.clearTemporaryError
  @Override
  public Valid isValid() {
    return valid;
  }

  public void set(final boolean valid) {
    set(Valid.fromBoolean(valid));
  }

  public void set(final Valid valid) {
    this.valid = valid;
    if (property != null) {
      property.reassess();
    }
  }

  public void setMessage(final String message) {
    this.message = message;
  }

}
