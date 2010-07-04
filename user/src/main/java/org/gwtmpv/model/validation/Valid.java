package org.gwtmpv.model.validation;

/** Validation state for models, properties, and rules. */
public enum Valid {

  /** The validation rule passed. */
  YES,

  /** The validation rule failed. */
  NO;

  public boolean isNo() {
    return this != YES;
  }

}
