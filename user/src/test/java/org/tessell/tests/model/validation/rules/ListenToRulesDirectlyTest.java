package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Required;

public class ListenToRulesDirectlyTest extends AbstractRuleTest {

  public static class Foo {
    public StringProperty name = stringProperty("name");
    public StringProperty description = stringProperty("description");
  }

  private final Foo f = new Foo();

  @Test
  public void listenToOne() {
    final Required r = new Required("name required");
    f.name.addRule(r);
    listenTo(r);

    f.name.set(null);
    assertMessages("name required");

    f.name.set("good name");
    assertMessages();
  }

}
