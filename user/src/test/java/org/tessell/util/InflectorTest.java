package org.tessell.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class InflectorTest {

  @Test
  public void humanize() {
    assertThat(Inflector.humanize("foo_bar"), is("Foo Bar"));
    assertThat(Inflector.humanize("FOO_BAR"), is("Foo Bar"));
    assertThat(Inflector.humanize("fooBar"), is("Foo Bar"));
    assertThat(Inflector.humanize("foo bar"), is("Foo Bar"));
    assertThat(Inflector.humanize("Foo Bar"), is("Foo Bar"));
    assertThat(Inflector.humanize("a_b"), is("A B"));
    assertThat(Inflector.humanize("aB"), is("A B"));
    assertThat(Inflector.humanize("ab"), is("Ab"));
    assertThat(Inflector.humanize("FOO"), is("Foo"));
    assertThat(Inflector.humanize("FOo"), is("Foo"));
  }

  @Test
  public void camelize() {
    assertThat(Inflector.camelize("Foo Bar"), is("fooBar"));
    assertThat(Inflector.camelize("FOO_BAR"), is("fooBar"));
    assertThat(Inflector.camelize("foo bar"), is("fooBar"));
    assertThat(Inflector.camelize("fooBar"), is("fooBar"));
  }

}
