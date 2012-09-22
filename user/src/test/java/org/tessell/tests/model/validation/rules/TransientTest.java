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
    p.addRule(new Transient<String>("p is invalid"));
    assertMessages("p is invalid");
  }

  @Test
  public void doesFireIfUntouched() {
    p.addRule(new Transient<String>("p is invalid"));
    assertMessages("");
  }

  @Test
  public void unfiresOnChange() {
    p.touch();
    p.addRule(new Transient<String>("p is invalid"));

    p.set("blah");
    assertMessages("");
  }

}
