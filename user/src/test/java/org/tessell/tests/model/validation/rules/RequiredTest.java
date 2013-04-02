package org.tessell.tests.model.validation.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.listProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Required;

public class RequiredTest extends AbstractRuleTest {

  // model class just for this test
  private class Foo {
    private final StringProperty name = stringProperty("name");
    private final BooleanProperty condition = booleanProperty("condition", true);
    private final ListProperty<String> colors = listProperty("colors");
  }

  private final Foo f = new Foo();

  @Before
  public void listenToName() {
    listenTo(f.name);
    listenTo(f.condition);
    listenTo(f.colors);
  }

  @Test
  public void ruleDoesNotFireIfPropertyUntouched() {
    assertThat(f.name.isTouched(), is(false));
    final Required r = new Required("name invalid");
    f.name.addRule(r);
    r.validate();
    assertMessages("");
  }

  @Test
  public void testTickedDoesFire() {
    Required r = new Required("name invalid");
    f.name.addRule(r);
    f.name.set(null);
    assertMessages("name invalid");
  }

  @Test
  public void testTickedUnfiresAfterValid() {
    Required r = new Required("name invalid");
    f.name.addRule(r);
    f.name.set(null);
    assertMessages("name invalid");

    f.name.set("asdf");
    assertMessages("");
  }

  @Test
  public void testUnfireIfSkippedLaterIsNotDoneAutomatically() {
    Required r = new Required("name invalid");
    r.onlyIf(f.condition);
    f.name.addRule(r);
    f.name.set(null);
    assertMessages("name invalid");

    // Just setting the onlyIf percolates down to our Required rule
    f.condition.set(false);
    assertMessages("");

    // was left touched, e.g. even if onlyIf=true, requires Force.YES
    f.condition.set(true);
    // reassessing f.name is required for now
    f.name.reassess();
    assertMessages("name invalid");
  }

  @Test
  public void requiredListsMustBeNonEmpty() {
    f.colors.addRule(new Required("colors required"));
    f.colors.touch();
    assertMessages("colors required");

    f.colors.add("Blue");
    assertNoMessages();
  }

  @Test
  public void requiredMarksPropertyRequired() {
    f.colors.addRule(new Required("colors required"));
    assertThat(f.colors.isRequired(), is(true));
  }

}
