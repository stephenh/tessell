package org.tessell.model.dsl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.dsl.WhenConditions.greaterThan;
import static org.tessell.model.dsl.WhenConditions.lessThan;
import static org.tessell.model.properties.NewProperty.integerProperty;

import org.junit.Test;
import org.tessell.model.properties.IntegerProperty;

public class WhenConditionsTest {

  @Test
  public void testGreaterThan() {
    IntegerProperty i = integerProperty("i", 1);
    assertThat(greaterThan(0).evaluate(i), is(true));
    assertThat(greaterThan(1).evaluate(i), is(false));
    assertThat(greaterThan(2).evaluate(i), is(false));
  }

  @Test
  public void testLessThan() {
    IntegerProperty i = integerProperty("i", 1);
    assertThat(lessThan(0).evaluate(i), is(false));
    assertThat(lessThan(1).evaluate(i), is(false));
    assertThat(lessThan(2).evaluate(i), is(true));
  }
}
