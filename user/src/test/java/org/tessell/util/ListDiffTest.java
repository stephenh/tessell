package org.tessell.util;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tessell.util.ListDiff.NewLocation;

public class ListDiffTest {

  @Test
  public void nullOldValue() {
    ListDiff<String> d = ListDiff.of(null, list("3", "3", "3"));
    assertThat(d.added.toString(), is("[3@0, 3@1, 3@2]"));
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
    assertThat(d.added.toString(), is("[3@0, 3@0]"));
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
    assertThat(d.moves.toString(), is("[2@0]"));
  }

  @Test
  public void testFindMoveInTheMiddle() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3", "4"), list("1", "3", "2", "4"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[3@1]"));
  }

  @Test
  public void testFindMoveAtTheStart() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3"), list("2", "1", "3"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[2@0]"));
  }

  @Test
  public void testFindMoveAtTheEnd() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3"), list("1", "3", "2"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    assertThat(d.moves.toString(), is("[3@1]"));
  }

  @Test
  public void testFindInsertInTheMiddle() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "4"), list("1", "2", "3", "4"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.toString(), is("[3@2]"));
    // if we add 3@2, 4 slides back, so we don't count it as a move
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void testFindRemoveInTheMiddle() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3", "4"), list("1", "2", "4"));
    assertThat(d.removed.toString(), is("[3]"));
    assertThat(d.added.size(), is(0));
    // if we remove 3, 4 slides up, so we don't count it as a move
    assertThat(d.moves.size(), is(0));
  }

  @Test
  public void testFindMovesWithMultipleMatchingValues() {
    ListDiff<String> d = ListDiff.of(list("1", "2", "3", "3"), list("3", "3", "1", "2"));
    assertThat(d.removed.size(), is(0));
    assertThat(d.added.size(), is(0));
    // the 2nd 3 here should really be "3@1", but, hey, if you're inserting these things
    // as value objects, then it probably doesn't matter that you'll .add(0, "3"), .add(0, "3")
    // twice.
    assertThat(d.moves.toString(), is("[3@0, 1@2, 2@3]"));
  }

  @Test
  public void testDiffs() {
    assertDiffIsRight(list("1", "2"), list("1"));
    assertDiffIsRight(list("1", "2"), list("2"));
    assertDiffIsRight(list("1", "2"), new ArrayList<String>());
    assertDiffIsRight(new ArrayList<String>(), list("1", "2"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("5", "4", "3", "2", "1"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("5", "4", "2", "1", "3"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("6", "5", "4", "3", "2", "1"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("6", "1", "2", "3", "4", "5"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("0", "1", "2", "3", "4", "5"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("1", "2", "3", "4", "5", "6"));
    assertDiffIsRight(list("1", "2", "3", "4", "5"), list("1", "2", "3", "!", "4", "5"));
  }

  private static void assertDiffIsRight(List<String> oldList, List<String> newList) {
    ListDiff<String> diff = ListDiff.of(oldList, newList);
    // make a copy of old
    List<String> copy = new ArrayList<String>(oldList);
    // apply any removes
    copy.removeAll(diff.removed);
    // apply any adds
    for (NewLocation<String> add : diff.added) {
      copy.add(add.index, add.element);
    }
    // apply any moves
    for (NewLocation<String> move : diff.moves) {
      copy.remove(move.element);
      copy.add(move.index, move.element);
    }
    assertThat(copy, is(newList));
  }

}
