package org.tessell.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListDiff<T> {

  public static <T> ListDiff<T> of(Collection<T> oldValue, Collection<T> newValue) {
    List<T> added = new ArrayList<T>();
    List<T> removed = new ArrayList<T>();
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
        }
      }
    }
    return new ListDiff<T>(added, removed);
  }

  public final Collection<T> added;
  public final Collection<T> removed;

  private ListDiff(Collection<T> added, Collection<T> removed) {
    this.added = added;
    this.removed = removed;
  }

  @Override
  public String toString() {
    return added + "; " + removed;
  }
}
