package org.tessell.util;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ListDiffTest {

  @Test
  public void testFindAdds() {
    ListDiff<String> d = ListDiff.of(list("3"), list("3", "3", "3"));
    assertThat(d.added.toString(), is("[3, 3]"));
    assertThat(d.removed.size(), is(0));
  }

  @Test
  public void testFindRemoves() {
    ListDiff<String> d = ListDiff.of(list("3", "3", "3"), list("3"));
    assertThat(d.removed.toString(), is("[3, 3]"));
    assertThat(d.added.size(), is(0));
  }

}
