package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;

import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.validation.rules.Static;
import org.junit.Before;
import org.junit.Test;

public class StaticTest extends AbstractRuleTest {

  final IntegerProperty i = integerProperty("i", 1);

  @Before
  public void listen() {
    listenTo(i);
    i.touch();
  }

  @Test
  public void firesOnSet() {
    Static rule = new Static(i, "is invalid");
    rule.set(false);
    assertMessages("is invalid");
  }

}
