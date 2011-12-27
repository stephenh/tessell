package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.integerProperty;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.validation.rules.Static;

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
