package org.tessell.bootstrap;

import static org.tessell.model.properties.NewProperty.integerProperty;

import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;

/**
 * Faciliates using Tessell's {@link Binder} methods again validation rules.
 * 
 * Specifically, we want to add/remove the {@code .error} CSS class to our form's control group when a property is
 * invalid. Tessell's properties don't have a built-in "am I invalid?" property, so this basically does that, watching
 * for rule trigger/untrigger events to maintain a count of outstanding errors.
 * 
 * When we can do something like {@code b.when(count).is(greaterThan(0)).set(error).on(element)}.
 * 
 * Should probably live somewhere inside of Tessell.
 */
public class ErrorCounter implements RuleTriggeredHandler, RuleUntriggeredHandler {

  public static ErrorCounter forProperty(final Property<?> p) {
    final ErrorCounter c = new ErrorCounter();
    p.addRuleTriggeredHandler(c);
    p.addRuleUntriggeredHandler(c);
    return c;
  }

  public static ErrorCounter forProperties(final Property<?>... properties) {
    final ErrorCounter c = new ErrorCounter();
    for (final Property<?> p : properties) {
      p.addRuleTriggeredHandler(c);
      p.addRuleUntriggeredHandler(c);
    }
    return c;
  }

  public final IntegerProperty count = integerProperty("count", 0);

  @Override
  public void onTrigger(final RuleTriggeredEvent event) {
    count.set(count.get() + 1);
  }

  @Override
  public void onUntrigger(final RuleUntriggeredEvent event) {
    count.set(count.get() - 1);
  }
}