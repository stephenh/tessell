package org.gwtmpv.model.dsl;

import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.widgets.IsTextList;

/** Binds specific rule outcomes to widgets, really only text lists. */
public class RuleBinder {

  private final Binder binder;
  private final Rule rule;

  public RuleBinder(final Binder binder, final Rule rule) {
    this.binder = binder;
    this.rule = rule;
  }

  public RuleBinder errorsTo(final IsTextList list) {
    final TextListOnError i = new TextListOnError(list);
    binder.registerHandler(rule.addRuleTriggeredHandler(i));
    binder.registerHandler(rule.addRuleUntriggeredHandler(i));
    return this;
  }
}
