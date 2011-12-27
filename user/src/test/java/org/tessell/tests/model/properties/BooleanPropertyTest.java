package org.tessell.tests.model.properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.booleanProperty;

import org.junit.Test;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class BooleanPropertyTest extends AbstractRuleTest {

  @Test
  public void toggleFromNullToTrue() {
    BooleanProperty p = booleanProperty("p");
    p.toggle();
    assertThat(p.get(), is(TRUE));
  }

  @Test
  public void toggleFromFalseToTrue() {
    BooleanProperty p = booleanProperty("p", false);
    p.toggle();
    assertThat(p.get(), is(TRUE));
  }

  @Test
  public void toggleFromTrueToFalse() {
    BooleanProperty p = booleanProperty("p", true);
    p.toggle();
    assertThat(p.get(), is(FALSE));
  }

}
