package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.rules.Required;
import org.junit.Test;

public class ListenToRulesDirectlyTest extends AbstractRuleTest {

  public static class Foo {
    public StringProperty name = stringProperty("name");
    public StringProperty description = stringProperty("description");
  }

  private final Foo f = new Foo();

  @Test
  public void listenToOne() {
    final Required r = new Required(f.name, "name required");
    listenTo(r);

    f.name.set(null);
    assertMessages("name required");

    f.name.set("good name");
    assertMessages();
  }

}