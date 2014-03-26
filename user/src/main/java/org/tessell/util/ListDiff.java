package org.tessell.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Compares new/old versions of lists, e.g. for firing add/remove events.
 */
public class ListDiff<T> {

  /** Maps one type to another (e.g. DTO to model, or model to view, or string to int). */
  public interface Mapper<A, B> {
    B map(A a);
  }

  /** A minimal list interface that we is all we need for {@link #apply(List)}. */
  public interface ListLike<A> {
    A remove(int index);

    void add(int index, A a);
  }

  /** @returns a diff of {@code oldValue} and {@code newValue}. */
  public static <T> ListDiff<T> of(List<T> oldValue, List<T> newValue) {
    List<Location<T>> added = new ArrayList<Location<T>>();
    List<Location<T>> moves = new ArrayList<Location<T>>();
    List<Location<T>> removed = new ArrayList<Location<T>>();
    if (oldValue == null && newValue != null) {
      int i = 0;
      for (T t : newValue) {
        added.add(new Location<T>(t, i++, -1));
      }
    } else if (oldValue != null && newValue == null) {
      for (T t : oldValue) {
        removed.add(new Location<T>(t, 0, -1));
      }
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
          removed.add(new Location<T>(t, oldCopy.indexOf(t), -1));
          i.remove();
        }
      }
      List<T> withRemoves = new ArrayList<T>(oldCopy);
      for (T t : newValue) {
        if (!oldCopy.remove(t)) {
          // we didn't find in old, so it's new
          int newIndex = newValue.indexOf(t);
          added.add(new Location<T>(t, newIndex, -1));
          // keep withRemoves up to date
          withRemoves.add(newIndex, t);
        } else {
          // we did find in old, but it might have moved
          int oldIndex = withRemoves.indexOf(t);
          int newIndex = newValue.indexOf(t);
          if (oldIndex != newIndex) {
            moves.add(new Location<T>(t, newIndex, oldIndex));
            // keep withRemoves up to date
            withRemoves.remove(oldIndex);
            withRemoves.add(newIndex, t);
          }
        }
      }
    }
    return new ListDiff<T>(added, moves, removed);
  }

  public final Collection<Location<T>> added;
  public final Collection<Location<T>> moves;
  public final Collection<Location<T>> removed;

  /** Tracks an element that was not added or moved to a new index in the list. */
  public static class Location<T> {
    public final T element;
    public final int index;
    /** The old index of {@code element}, *after* any removals are applied. */
    public final int oldIndex;

    private Location(T element, int index, int oldIndex) {
      this.element = element;
      this.index = index;
      this.oldIndex = oldIndex;
    }

    @Override
    public String toString() {
      return element + "@" + index;
    }
  }

  /** Brings an old list {@code copy} up to date with our new value by applying adds/removes. */
  public void apply(List<T> copy) {
    // call the overload with an identity mapper
    apply(copy, new Mapper<T, T>() {
      public T map(T a) {
        return a;
      }
    });
  }

  public <U> void apply(final List<U> copy, Mapper<T, U> mapper) {
    // sigh, no structural typing
    apply(new ListLike<U>() {
      public U remove(int index) {
        return copy.remove(index);
      }

      public void add(int index, U a) {
        copy.add(index, a);
      }
    }, mapper);
  }

  public <U> void apply(ListLike<U> copy, Mapper<T, U> mapper) {
    // apply any removes
    for (Location<T> remove : removed) {
      copy.remove(remove.index);
    }
    // apply any adds
    for (Location<T> add : added) {
      copy.add(add.index, mapper.map(add.element));
    }
    // apply any moves
    for (Location<T> move : moves) {
      copy.add(move.index, copy.remove(move.oldIndex));
    }
  }

  private ListDiff(Collection<Location<T>> added, Collection<Location<T>> moves, Collection<Location<T>> removed) {
    this.added = added;
    this.removed = removed;
    this.moves = moves;
  }

  @Override
  public String toString() {
    return added + "; " + moves + "; " + removed;
  }
}
