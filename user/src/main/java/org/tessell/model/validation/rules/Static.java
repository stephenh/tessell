package org.tessell.model.validation.rules;


/** A rule that you can explicitly set to valid or invalid as needed. */
public class Static extends AbstractRule<Object> {

  private boolean valid = true;

  public Static(final String message) {
    super(message);
  }

  // promoted to public access for AbstractRule.clearTemporaryError
  @Override
  public boolean isValid() {
    return valid;
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
