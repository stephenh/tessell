package org.gwtmpv.model.dsl;

import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.widgets.IsTextList;

/** Binds specific rule outcomes to widgets, really only text lists. */
public class RuleBinder {

  private final Rule rule;

  public RuleBinder(final Rule rule) {
    this.rule = rule;
  }

  public HandlerRegistrations errorsTo(final IsTextList list) {
    final TextListOnError i = new TextListOnError(list);
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(rule.addRuleTriggeredHandler(i));
    hr.add(rule.addRuleUntriggeredHandler(i));
    return hr;
  }
}
