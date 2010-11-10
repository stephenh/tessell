package org.gwtmpv.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class InflectorTest {

  @Test
  public void humanize() {
    assertThat(Inflector.humanize("foo_bar"), is("Foo Bar"));
    assertThat(Inflector.humanize("fooBar"), is("Foo Bar"));
    assertThat(Inflector.humanize("a_b"), is("A B"));
    assertThat(Inflector.humanize("aB"), is("A B"));
    assertThat(Inflector.humanize("ab"), is("Ab"));
  }

  @Test
  public void camelize() {
    assertThat(Inflector.camelize("Foo Bar"), is("fooBar"));
  }

}
