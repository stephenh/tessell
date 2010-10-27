package org.gwtmpv.model.validation.rules;

import org.gwtmpv.model.properties.FormattedProperty;
import org.gwtmpv.model.properties.HasRuleTriggers;
import org.gwtmpv.model.validation.Valid;

/** A validation rule. */
public interface Rule extends HasRuleTriggers {

  /** @return whether this rule is valid */
  Valid validate();

  /** Ugly hack to put static rule in {@link FormattedProperty} before required rules. */
  boolean isImportant();

}
