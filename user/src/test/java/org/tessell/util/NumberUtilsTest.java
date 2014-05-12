package org.tessell.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import org.junit.Test;

public class NumberUtilsTest {

  @Test
  public void testFormatLong() {
    assertThat(NumberUtils.format(-100), is("-100"));
    assertThat(NumberUtils.format(0), is("0"));
    assertThat(NumberUtils.format(100), is("100"));
    assertThat(NumberUtils.format(1000), is("1,000"));
    assertThat(NumberUtils.format(1000000000), is("1,000,000,000"));
  }

  @Test
  public void testFormatDouble() {
    assertThat(NumberUtils.format(-100, 2), is("-100.00"));
    assertThat(NumberUtils.format(0, 0), is("0"));
    assertThat(NumberUtils.format(0, 2), is("0.00"));
    assertThat(NumberUtils.format(100.0, 0), is("100"));
    assertThat(NumberUtils.format(100.6, 0), is("101"));
    assertThat(NumberUtils.format(100.1, 2), is("100.10"));
    assertThat(NumberUtils.format(10.1234, 2), is("10.12"));
    assertThat(NumberUtils.format(1000.123, 2), is("1,000.12"));
    assertThat(NumberUtils.format(1000.126, 2), is("1,000.13"));
  }

  @Test
  public void testParseDouble() throws Exception {
    assertThat(NumberUtils.parse("0.", 2), is(0.0));
    assertThat(NumberUtils.parse(".0", 2), is(0.0));
    assertThat(NumberUtils.parse("0.0", 2), is(0.0));
    assertThat(NumberUtils.parse("0.10", 2), is(0.10));
    assertThat(NumberUtils.parse("0.10001", 2), is(0.10));
    assertThat(NumberUtils.parse("1.10", 2), is(1.10));
    assertThat(NumberUtils.parse("1.3", 2), is(1.30));
    assertThat(NumberUtils.parse("1.33", 2), is(1.33));
    assertThat(NumberUtils.parse("1.335", 2), is(1.34));
    assertThat(NumberUtils.parse("-1.33", 2), is(-1.33));
    assertThat(NumberUtils.parse("1,333", 2), is(1333.0));
    assertThat(NumberUtils.parse("-1,333", 2), is(-1333.0));
    assertNumberFormatException("");
    assertNumberFormatException(".");
  }

  private static void assertNumberFormatException(final String value) {
    try {
      NumberUtils.parse(value, 2);
      fail();
    } catch (final NumberFormatException nfe) {
    }
  }

}
