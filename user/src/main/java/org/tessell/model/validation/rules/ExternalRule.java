package org.tessell.model.validation.rules;

import org.tessell.model.validation.Valid;

public class ExternalRule extends Static {

  public ExternalRule(String message) {
    super(message);
  }

  @Override
  public void triggerIfNeeded(Valid validationResult) {
    //re-fire triggered event for Valid.PENDING -> Valid.FALSE transition
    untriggerIfNeeded();
    super.triggerIfNeeded(validationResult);
  }
}
