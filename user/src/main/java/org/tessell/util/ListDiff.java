package org.tessell.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Compares new/old versions of lists, e.g. for firing add/remove events.
 */
public class ListDiff<T> {

  /** @returns a diff of {@code oldValue} and {@code newValue}. */
  public static <T> ListDiff<T> of(List<T> oldValue, List<T> newValue) {
    List<T> added = new ArrayList<T>();
    List<T> removed = new ArrayList<T>();
    List<NewLocation<T>> moves = new ArrayList<NewLocation<T>>();
    if (oldValue == null && newValue != null) {
      added.addAll(newValue);
    } else if (oldValue != null && newValue == null) {
      removed.addAll(oldValue);
    } else if (oldValue != null && newValue != null) {
      List<T> newCopy = new ArrayList<T>(newValue);
      for (T t : oldValue) {
        if (!newCopy.remove(t)) {
          removed.add(t);
        }
      }
      List<T> oldCopy = new ArrayList<T>(oldValue);
      for (T t : newValue) {
        if (!oldCopy.remove(t)) {
          added.add(t);
        } else {
          int oldIndex = oldValue.indexOf(t);
          int newIndex = newValue.indexOf(t);
          if (oldIndex != newIndex) {
            moves.add(new NewLocation<T>(t, newIndex));
          }
        }
      }
    }
    return new ListDiff<T>(added, removed, moves);
  }

  public final Collection<T> added;
  public final Collection<T> removed;
  public final Collection<NewLocation<T>> moves;

  /** Tracks an element that was not added or removed, but moved to a new index in the list. */
  public static class NewLocation<T> {
    public final T element;
    public final int index;

    private NewLocation(T element, int index) {
      this.element = element;
      this.index = index;
    }

    @Override
    public String toString() {
      return element + "@" + index;
    }
  }

  private ListDiff(Collection<T> added, Collection<T> removed, Collection<NewLocation<T>> moves) {
    this.added = added;
    this.removed = removed;
    this.moves = moves;
  }

  @Override
  public String toString() {
    return added + "; " + removed + "; " + moves;
  }
}
