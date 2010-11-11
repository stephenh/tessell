package org.gwtmpv.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

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
