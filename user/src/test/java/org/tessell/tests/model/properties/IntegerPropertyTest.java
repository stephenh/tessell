package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.tessell.model.properties.NewProperty.integerProperty;

import org.junit.Test;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.validation.Valid;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class IntegerPropertyTest extends AbstractRuleTest {

  @Test
  public void fromStringIsResetWhenGood() {
    final IntegerProperty p = integerProperty("p");
    p.asString().set("blah");
    assertThat(p.wasValid(), is(Valid.NO));
    p.set(1);
    assertThat(p.wasValid(), is(Valid.YES));
  }

  @Test
  public void fromStringWithNull() {
    final IntegerProperty p = integerProperty("p", 1);
    p.asString().set(null);
    assertThat(p.get(), is(nullValue()));
    assertThat(p.wasValid(), is(Valid.YES));
  }

  @Test
  public void callingAsStringTwiceShouldNotResultInMultipleErrors() {
    final IntegerProperty p = integerProperty("p", 1);
    listenTo(p);
    p.asString().set("2"); // good
    p.asString().set("asdf"); // bad
    assertMessages("P must be an integer");
  }

}
