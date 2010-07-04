package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.validation.Valid;
import org.junit.Test;

public class IntegerPropertyTest {

  @Test
  public void fromStringIsResetWhenGood() {
    final IntegerProperty p = integerProperty("p");
    p.setAsString("blah");
    assertThat(p.wasValid(), is(Valid.NO));
    p.set(1);
    assertThat(p.wasValid(), is(Valid.YES));
  }

  @Test
  public void fromStringIsResetWhenGoodViaInitial() {
    final IntegerProperty p = integerProperty("p");
    p.setAsString("blah");
    assertThat(p.wasValid(), is(Valid.NO));
    p.setInitial(1);
    assertThat(p.wasValid(), is(Valid.YES));
  }

}
