package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Transient;

public class TransientTest extends AbstractRuleTest {

  final StringProperty p = listenTo(stringProperty("p"));

  @Test
  public void firesImmediatelyIfTouched() {
    p.touch();
    new Transient<String>(p, "p is invalid");
    assertMessages("p is invalid");
  }

  @Test
  public void doesFireIfUntouched() {
    new Transient<String>(p, "p is invalid");
    assertMessages("");
  }

  @Test
  public void unfiresOnChange() {
    p.touch();
    new Transient<String>(p, "p is invalid");

    p.set("blah");
    assertMessages("");
  }

}
