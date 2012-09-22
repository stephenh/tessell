package org.tessell.tests.model.validation.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
import org.tessell.model.validation.Valid;
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
    new Required(f.name, "name required");
    new Length(f.name, "name length");

    assertThat(f.name.wasValid(), nullValue());
    f.name.set(null);
    assertThat(f.name.wasValid(), is(Valid.NO));
  }

  @Test
  public void whenMultiplesRulesPerPropertyOnlyTheFirstFires() {
    new Required(f.name, "name required");
    new Length(f.name, "name length");

    f.name.set(null);
    assertMessages("name required");
  }

  @Test
  public void whenMultipleRulesPerPropertyTheSecondCanFireNext() {
    new Required(f.name, "name required");
    new Length(f.name, "name length");

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

    new Custom(f.name, "custom", customLogic);
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
    new Custom(f.name, "name cannot be bob", new Supplier<Boolean>() {
      public Boolean get() {
        return !eq(f.name.get(), "bob");
      }
    });

    f.name.set("bob");
    assertMessages("name cannot be bob");

    f.name.set("fred");
    assertNoMessages();
  }

  @Test
  public void untouchingUntriggersTheRule() {
    new Required(f.name, "name required");

    f.name.set(null);
    assertMessages("name required");

    f.name.setTouched(false);
    assertMessages("");
  }

  @Test
  public void onlyIfDoesNotTouchDownstreamRules() {
    final BooleanProperty b = booleanProperty("b", false);
    new Required(f.name, "name required").onlyIf(b);
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
  public void onlyIfDoesUpstreamTracking() {
    final BooleanProperty b = booleanProperty("b", false);
    new Required(f.name, "name required").onlyIf(b);
    f.name.touch();
    assertMessages("");
    b.set(true);
    assertMessages("name required");
  }

  @Test
  public void onlyIfAfterAlreadyInvalid() {
    Required r = new Required(f.name, "name required");
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
    Required r = new Required(f.name, "name required");
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
    new Custom(c, "c failed", new Supplier<Boolean>() {
      public Boolean get() {
        return a.isTrue() && b.isTrue();
      }
    });
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
    Required r = new Required(f.name, "name required");
    f.name.addRule(r);

    f.name.set(null);
    assertMessages("name required");
  }

  @Test
  public void ruleHandlersAreFiredBeforeThePropertysHandlers() {
    Required r = new Required(f.name, "name required");

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
    new Length(f.name, "name length");
    f.name.set(null);
    assertNoMessages();
  }

  @Test
  public void tracksUpstreamDependencies() {
    new Custom(f.name, "cannot be the description", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    });
    f.description.set("a");
    f.name.set("a");
    assertMessages("cannot be the description");
    // just changing description, the rules for a get rerun
    f.description.set("b");
    assertNoMessages();
  }

  @Test
  public void tracksTwoWayUpstreamDependencies() {
    new Custom(f.name, "cannot be the description", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    });
    new Custom(f.description, "cannot be the name", new Supplier<Boolean>() {
      public Boolean get() {
        return f.description.get() == null || f.name.get() == null || !f.name.get().equals(f.description.get());
      }
    });
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
