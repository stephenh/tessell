package org.tessell.model.validation.rules;

import org.tessell.model.properties.FormattedProperty;
import org.tessell.model.properties.HasRuleTriggers;
import org.tessell.model.validation.Valid;

/** A validation rule. */
public interface Rule extends HasRuleTriggers {

  /** @return whether this rule is valid */
  Valid validate();

  /** Ugly hack to put static rule in {@link FormattedProperty} before required rules. */
  boolean isImportant();

  void untriggerIfNeeded();

  void triggerIfNeeded();

}
