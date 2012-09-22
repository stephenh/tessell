package org.tessell.model.dsl;

import org.tessell.model.validation.rules.Rule;
import org.tessell.widgets.IsTextList;

/** Binds specific rule outcomes to widgets, really only text lists. */
public class RuleBinder<P> {

  private final Binder b;
  private final Rule<P> rule;

  public RuleBinder(final Binder b, final Rule<P> rule) {
    this.b = b;
    this.rule = rule;
  }

  public void errorsTo(final IsTextList list) {
    final TextListOnError i = new TextListOnError(list);
    b.add(rule.addRuleTriggeredHandler(i));
    b.add(rule.addRuleUntriggeredHandler(i));
  }
}
