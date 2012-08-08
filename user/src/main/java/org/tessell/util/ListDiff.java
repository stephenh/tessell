package org.tessell.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListDiff<T> {

  public static <T> ListDiff<T> of(Collection<T> oldValue, Collection<T> newValue) {
    List<T> added = new ArrayList<T>();
    List<T> removed = new ArrayList<T>();
    if (oldValue == null) {
      added.addAll(newValue);
      return new ListDiff<T>(added, removed);
    }
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
      }
    }
    return new ListDiff<T>(added, removed);
  }

  public final Collection<T> added;
  public final Collection<T> removed;

  private ListDiff(List<T> added, List<T> removed) {
    this.added = added;
    this.removed = removed;
  }

  @Override
  public String toString() {
    return added + "; " + removed;
  }
}
