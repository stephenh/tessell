package org.gwtmpv.tests.model.validation.rules;

import org.junit.Before;
import org.junit.Test;

public class RuleGroupTest extends AbstractRuleTest {

  private final Foo f = new Foo();

  @Before
  public void listenToNameAndDesc() {
    listenTo(f.name, f.description, f.all);
  }

  @Test
  public void bothInAGroupIsInValid() {
    f.all.touch();
    assertMessages("name required", "description required", "some invalid");
  }

  @Test
  public void oneInAGroupIsInValid() {
    f.name.set("somename");
    f.all.touch();
    assertMessages("description required", "some invalid");
  }

  @Test
  public void noForceDoesNotTriggerChildren() {
    f.all.validate();
    assertMessages();
  }

  @Test
  public void invalidInAGroupBecomingValidUnfiresGroup() {
    f.name.set("somename");
    f.all.touch();
    assertMessages("description required", "some invalid");
    f.description.set("somedesc");
    assertMessages();
  }

}