package org.tessell.model.validation.rules;

import org.tessell.model.properties.FormattedProperty;
import org.tessell.model.properties.HasRuleTriggers;
import org.tessell.model.properties.Property;
import org.tessell.model.values.Value;

/** A validation rule. */
public interface Rule<P> extends HasRuleTriggers {

  /** Establishes the property to evaluate against, will be called before validate. */
  void setProperty(Property<P> property);

  /** @return whether this rule is valid */
  boolean validate();

  /** Ugly hack to put static rule in {@link FormattedProperty} before required rules. */
  boolean isImportant();

  void untriggerIfNeeded();

  void triggerIfNeeded();

  void onlyIf(final Value<Boolean> value);

}
