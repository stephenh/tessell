package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

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

  @Test
  public void lenCreatesARule() {
    final StringProperty p = stringProperty("p").len(2, 5);
    listenTo(p);
    p.set("123456");
    assertMessages("P must be between 2 and 5");

    p.set("1");
    assertMessages("P must be between 2 and 5");

    p.set("12");
    assertMessages("");
  }

  @Test
  public void lenSetsMaxLength() {
    final StringProperty p = stringProperty("p").len(2, 5);
    assertThat(p.getMaxLength(), is(5));
  }

  @Test
  public void regexMatches() {
    final StringProperty p = stringProperty("p").regex("[a-z]", "did not match");
    listenTo(p);
    p.set("a");
    assertMessages("");
  }

  @Test
  public void regexDoesNotMatch() {
    final StringProperty p = stringProperty("p").regex("[a-z]", "did not match");
    listenTo(p);
    p.set("1");
    assertMessages("did not match");
  }

}
