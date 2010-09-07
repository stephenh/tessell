package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;

import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.rules.Required;
import org.junit.Before;
import org.junit.Test;

public class RequiredTest extends AbstractRuleTest {

  // model class just for this test
  private class Foo {
    private final StringProperty name = stringProperty("name");
    private final BooleanProperty condition = booleanProperty("condition", true);
  }

  private final Foo f = new Foo();

  @Before
  public void listenToName() {
    listenTo(f.name);
  }

  @Test
  public void ruleDoesNotFireIfPropertyUntouched() {
    assertThat(f.name.isTouched(), is(false));
    final Required r = new Required(f.name, "name invalid");
    r.validate();
    assertMessages("");
  }

  @Test
  public void ruleDoesFireIfPropertyTouched() {
    f.name.touch();
    final Required r = new Required(f.name, "name invalid");
    r.validate();
    assertMessages("name invalid");
  }

  @Test
  public void testTickedDoesFire() {
    new Required(f.name, "name invalid");
    f.name.set(null);
    assertMessages("name invalid");
  }

  @Test
  public void testTickedUnfiresAfterValid() {
    new Required(f.name, "name invalid");
    f.name.set(null);
    assertMessages("name invalid");

    f.name.set("asdf");
    assertMessages("");
  }

  @Test
  public void testUnfireIfSkippedLater() {
    new Required(f.name, "name invalid").onlyIf(f.condition);
    f.name.set(null);
    assertMessages("name invalid");

    // Just setting the onlyIf percolates down to our Required rule
    f.condition.set(false);
    assertMessages("");

    // was left touched, e.g. even if onlyIf=true, requires Force.YES
    f.condition.set(true);
    assertMessages("name invalid");
  }

  @Test
  public void testTickedDoesNotFireIfAlreadyInvalid() {
    final Required r = new Required(f.name, "name invalid second");
    f.name.set(null);
    Assert.assertEquals(Valid.NO, r.validate());
    assertMessages("");
  }

  @Test
  public void testUnfiresIfAlreadyInvalid() {
    final Required r1 = new Required(f.name, "name invalid");
    f.name.set(null);
    assertMessages("name invalid");
    Assert.assertEquals(Valid.NO, r1.validate());
    assertMessages("");
  }

}
