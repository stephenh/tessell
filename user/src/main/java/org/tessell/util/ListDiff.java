package org.tessell.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Compares new/old versions of lists, e.g. for firing add/remove events.
 */
public class ListDiff<T> {

  /** @returns a diff of {@code oldValue} and {@code newValue}. */
  public static <T> ListDiff<T> of(List<T> oldValue, List<T> newValue) {
    List<NewLocation<T>> added = new ArrayList<NewLocation<T>>();
    List<NewLocation<T>> moves = new ArrayList<NewLocation<T>>();
    List<T> removed = new ArrayList<T>();
    if (oldValue == null && newValue != null) {
      int i = 0;
      for (T t : newValue) {
        added.add(new NewLocation<T>(t, i++));
      }
    } else if (oldValue != null && newValue == null) {
      removed.addAll(oldValue);
    } else if (oldValue != null && newValue != null) {
      // We make oldCopy/newCopy so that we can call .remove on
      // the copies (a destructive operation) so that we'll detect
      // multiple entries of primitives/value objects.
      List<T> oldCopy = new ArrayList<T>(oldValue);
      // First find removals, and make oldCopy contain only newValues
      List<T> newCopy = new ArrayList<T>(newValue);
      for (Iterator<T> i = oldCopy.iterator(); i.hasNext();) {
        T t = i.next();
        if (!newCopy.remove(t)) {
          removed.add(t);
          i.remove();
        }
      }
      List<T> withRemoves = new ArrayList<T>(oldCopy);
      for (T t : newValue) {
        if (!oldCopy.remove(t)) {
          // we didn't find in old, so it's new
          int newIndex = newValue.indexOf(t);
          added.add(new NewLocation<T>(t, newIndex));
          // keep withRemoves up to date
          withRemoves.add(newIndex, t);
        } else {
          // we did find in old, but it might have moved
          int oldIndex = withRemoves.indexOf(t);
          int newIndex = newValue.indexOf(t);
          if (oldIndex != newIndex) {
            moves.add(new NewLocation<T>(t, newIndex));
            // keep withRemoves up to date
            withRemoves.remove(oldIndex);
            withRemoves.add(newIndex, t);
          }
        }
      }
    }
    return new ListDiff<T>(added, moves, removed);
  }

  public final Collection<NewLocation<T>> added;
  public final Collection<NewLocation<T>> moves;
  public final Collection<T> removed;

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

  private ListDiff(Collection<NewLocation<T>> added, Collection<NewLocation<T>> moves, Collection<T> removed) {
    this.added = added;
    this.removed = removed;
    this.moves = moves;
  }

  @Override
  public String toString() {
    return added + "; " + moves + "; " + removed;
  }
}
