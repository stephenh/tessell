package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class StringPropertyTest extends AbstractRuleTest {

  @Test
  public void maxCreatesARule() {
    final StringProperty p = stringProperty("p").max(2);
    listenTo(p);
    p.set("abc");
    assertMessages("P must be less than 2");

    p.set("ab");
    assertMessages("");
  }

}
