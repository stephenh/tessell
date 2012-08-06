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

    b.set(true);
    assertMessages("");

    f.name.touch();
    assertMessages("name required");
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
}
