package org.tessell.util;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ListDiffTest {

  @Test
  public void nullOldValue() {
    ListDiff<String> d = ListDiff.of(null, list("3", "3", "3"));
    assertThat(d.added.toString(), is("[3, 3, 3]"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void nullNewValue() {
    ListDiff<String> d = ListDiff.of(list("3", "3", "3"), null);
    assertThat(d.removed.toString(), is("[3, 3, 3]"));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void nullNewAndOldValue() {
    ListDiff<String> d = ListDiff.of(null, null);
    assertThat(d.added.size(), is(0));
    assertThat(d.removed.size(), is(0));
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void testFindAdds() {
    ListDiff<String> d = ListDiff.of(list("3"), list("3", "3", "3"));
    assertThat(d.added.toString(), is("[3, 3]"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void testFindRemoves() {
    ListDiff<String> d = ListDiff.of(list("3", "3", "3"), list("3"));
    assertThat(d.removed.toString(), is("[3, 3]"));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void testFindMoves() {
    ListDiff<String> d = ListDiff.of(list("1", "2"), list("2", "1"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[2@0, 1@1]"));
  }

  @Test
  public void testFindMoveAtTheStart() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3"), list("2", "1", "3"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[2@0, 1@1]"));
  }

  @Test
  public void testFindMoveAtTheEnd() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3"), list("1", "3", "2"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[3@1, 2@2]"));
  }

  @Test
  public void testFindMovesWithMultipleMatchingValues() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3", "3"), list("3", "3", "1", "2"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    // the 2nd 3 here should really be "3@1", but, hey, if you're inserting these things
    // as value objects, then it probably doesn't matter that you'll .add(0, "3"), .add(0, "3")
    // twice.
    assertThat(d.moves.toString(), is("[3@0, 3@0, 1@2, 2@3]"));
  }

}
