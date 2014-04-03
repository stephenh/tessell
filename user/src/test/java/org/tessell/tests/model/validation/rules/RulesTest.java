package org.tessell.tests.model.validation.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.util.ObjectUtils.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.rules.Custom;
import org.tessell.model.validation.rules.Length;
import org.tessell.model.validation.rules.Required;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.util.Supplier;

public class RulesTest extends AbstractRuleTest {

  public static class Foo {
    public StringProperty name = stringProperty("name");
    public StringProperty description = stringProperty("description");
  }

  private final Foo f = new Foo();

  @Before
  public void listenToName() {
    listenTo(f.name);
    listenTo(f.description);
  }

  @Test
  public void whenRuleIsValidatedTheStateIsRemembered() {
    assertThat(f.name.isValid(), is(true));

    f.name.addRule(new Required("name required"));
    f.name.addRule(new Length("name length"));
    assertThat(f.name.isValid(), is(false));

    f.name.set(null);
    assertThat(f.name.isValid(), is(false));
  }

  @Test
  public void ruleReassessAfterAdded() {
    f.name.touch();
    f.name.addRule(new Required("name required"));
    assertMessages("name required");
  }

  @Test
  public void whenMultiplesRulesPerPropertyOnlyTheFirstFires() {
    f.name.addRule(new Required("name required"));
    f.name.addRule(new Length("name length"));

    f.name.set(null);
    assertMessages("name required");
  }

  @Test
  public void whenMultipleRulesPerPropertyTheSecondCanFireNext() {
    f.name.addRule(new Required("name required"));
    f.name.addRule(new Length("name length"));

    f.name.set(StringUtils.repeat("a", 101));
    assertMessages("name length");
    // back to a good value
    f.name.set("good value");
    assertMessages("");
  }

  @Test
  public void reassessingReValidatesForCustomRules() {
    final SetValue<Boolean> customLogic = new SetValue<Boolean>("customLogic");
    customLogic.set(true);

    f.name.addRule(new Custom("custom", customLogic));
    f.name.set("a name");
    assertMessages("");

    // just changing the logic has no way of revalidating
    customLogic.set(false);
    assertMessages("");

    // but reassessing should
    f.name.reassess();
    assertMessages("custom");
  }

  @Test
  public void customWithSupplier() {
    f.name.addRule(new Custom("name cannot be bob", new Supplier<Boolean>() {
      public Boolean get() {
        return !eq(f.name.get(), "bob");
      }
    }));

    f.name.set("bob");
    assertMessages("name cannot be bob");

    f.name.set("fred");
    assertNoMessages();
  }

  @Test
  public void requiredSubclassWillBeTrackUpstreamValues() {
    f.name.addRule(new Required("Required") {
      protected boolean isValid() {
        return f.description.get() != null;
      }
    });

    f.name.touch();
    assertMessages("Required");

    f.description.set("some description");
    assertNoMessages();
  }

  @Test
  public void untouchingUntriggersTheRule() {
    f.name.addRule(new Required("name required"));

    f.name.set(null);
    assertMessages("name required");

    f.name.setTouched(false);
    assertMessages("");
  }

  @Test
  public void onlyIfDoesNotTouchDownstreamRules() {
    final BooleanProperty b = booleanProperty("b", false);
    Required required = new Required("name required");
    required.onlyIf(b);
    f.name.addRule(required);
    // touching b leaves f.name untouched
    b.set(true);
    assertMessages("");
    // but once we explicitly touch it, the rule runs
    f.name.touch();
    assertMessages("name required");
    // and toggling off b fixes it
    b.set(false);
    assertMessages("");
  }

  @Test
  public void onlyIfDoesNotTouchDownstreamRulesWhenAlreadyTouched() {
    final BooleanProperty b = booleanProperty("b");
    // start out with b already touched
    b.set(true);
    Required required = new Required("name required");
    required.onlyIf(b);
    f.name.addRule(required);
    assertThat(f.name.isTouched(), is(false));
  }

  @Test
  public void onlyIfDoesUpstreamTracking() {
    final BooleanProperty b = booleanProperty("b", false);
    Required r = new Required("name required");
    r.onlyIf(b);
    f.name.addRule(r);
    f.name.touch();
    assertMessages("");
    b.set(true);
    assertMessages("name required");
  }

  @Test
  public void onlyIfAfterAlreadyInvalid() {
    Required r = new Required("name required");
    f.name.addRule(r);
    f.name.touch();
    assertMessages("name required");
    // now add the only if
    final BooleanProperty b = booleanProperty("b", false);
    r.onlyIf(b);
    // which caused Required to be untriggered
    assertMessages("");
  }

  @Test
  public void onlyIfUpgradesNonTouchDerivedProperties() {
    Required r = new Required("name required");
    f.name.addRule(r);
    final BooleanProperty b = booleanProperty("b", true);
    r.onlyIf(b);
    // touching b at this point doesn't touch f.name
    b.touch();
    assertMessages("");
    // but after explicitly adding it
    b.addDerived(f.name);
    // now it is touched
    assertMessages("name required");
  }

  @Test
  public void removeDerivedHandlesMultipleDownstream() {
    final BooleanProperty a = booleanProperty("a", true);
    final BooleanProperty b = booleanProperty("b", true);
    // setup c to depend on b
    final BooleanProperty c = booleanProperty(new DerivedValue<Boolean>("c") {
      public Boolean get() {
        return b.isTrue();
      }
    });
    listenTo(c);
    // setup a rule that depends on A and B
    c.addRule(new Custom("c failed", new Supplier<Boolean>() {
      public Boolean get() {
        return a.isTrue() && b.isTrue();
      }
    }));
    // reassess so that c depends on a && b
    c.reassess();
    assertNoMessages();
    // at this point, c is downstream of b because of it's value and the rule
    // so, change a to false, which will remove 1 of the downstream tokens from c's upstream
    a.set(false);
    // so and now if we change b to false
    b.set(false);
    // c will still have been re-evaled and marked as failing
    assertMessages("c failed");
  }

  @Test
  public void rulesCanBeAddedTwice() {
    Required r = new Required("name required");
    f.name.addRule(r);

    f.name.set(null);
    assertMessages("name required");
  }

  @Test
  public void ruleHandlersAreFiredBeforeThePropertysHandlers() {
    Required r = new Required("name required");
    f.name.addRule(r);

    final List<String> order = new ArrayList<String>();
    r.addRuleTriggeredHandler(new RuleTriggeredHandler() {
      public void onTrigger(RuleTriggeredEvent event) {
        order.add("rule");
      }
    });
    f.name.addRuleTriggeredHandler(new RuleTriggeredHandler() {
      public void onTrigger(RuleTriggeredEvent event) {
        order.add("property");
      }
    });

    f.name.touch();
    assertThat(order, contains("rule", "property"));
  }

  @Test
  public void whenLengthOnlyThenEmptyIsOkay() {
    f.name.addRule(new Length("name length"));
    f.name.set(null);
    assertNoMessages();
  }

  @Test
  public void tracksUpstreamDependencies() {
    f.name.addRule(new Custom("cannot be the description", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    }));
    f.description.set("a");
    f.name.set("a");
    assertMessages("cannot be the description");
    // just changing description, the rules for a get rerun
    f.description.set("b");
    assertNoMessages();
  }

  @Test
  public void tracksTwoWayUpstreamDependencies() {
    f.name.addRule(new Custom("cannot be the description", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    }));
    f.description.addRule(new Custom("cannot be the name", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    }));
    f.description.set("a");
    f.name.set("a");
    assertMessages("cannot be the description", "cannot be the name");
    // just changing description reassesses both properties
    f.description.set("b");
    assertNoMessages();
    // also changing name reassesses both properties
    f.name.set("b");
    assertMessages("cannot be the description", "cannot be the name");
  }
}
