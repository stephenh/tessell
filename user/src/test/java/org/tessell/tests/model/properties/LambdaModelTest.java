package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class LambdaModelTest {

  @Test
  public void lambdas() {
    SomeDto dto = new SomeDto("foo", false);
    SomeModel m = new SomeModel(dto);
    assertThat(m.name.get(), is("foo"));
    m.name.set("bar");
    assertThat(dto.name, is("bar"));
  }
}
