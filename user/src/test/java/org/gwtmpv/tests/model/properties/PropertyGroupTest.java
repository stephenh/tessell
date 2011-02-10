package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class PropertyGroupTest extends AbstractRuleTest {

  @Test
  public void touchesAllChildren() {
    PropertyGroup all = new PropertyGroup("all", "some message");
    StringProperty p1 = stringProperty("p1").in(all);
    StringProperty p2 = stringProperty("p1").in(all);
    assertThat(p1.isTouched(), is(false));
    assertThat(p2.isTouched(), is(false));

    all.touch();
    assertThat(p1.isTouched(), is(true));
    assertThat(p2.isTouched(), is(true));
  }

  @Test
  public void isInvalidIfChildIsInvalid() {
    PropertyGroup all = new PropertyGroup("all", "some message");
    StringProperty p1 = stringProperty("p1").in(all).max(5);

    p1.set("123456");
    assertThat(all.wasValid(), is(Valid.NO));
  }

  @Test
  public void becomesValidIfChildIsValid() {
    PropertyGroup all = new PropertyGroup("all", "some message");
    StringProperty p1 = stringProperty("p1").in(all).max(5);

    p1.set("123456");
    assertThat(all.wasValid(), is(Valid.NO));

    p1.set("1234");
    assertThat(all.wasValid(), is(Valid.YES));
  }

  @Test
  public void becomesValidOnRemovalOfInvalidChild() {
    PropertyGroup all = new PropertyGroup("all", "some message");
    StringProperty p1 = stringProperty("p1").in(all).max(5);

    p1.set("123456");
    assertThat(all.wasValid(), is(Valid.NO));

    all.remove(p1);
    assertThat(all.wasValid(), is(Valid.YES));

    // flipping p1 valid/invalid doesn't change all
    p1.set("1234");
    p1.set("123456");
    assertThat(all.wasValid(), is(Valid.YES));
  }

}
