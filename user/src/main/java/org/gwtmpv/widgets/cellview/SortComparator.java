package org.gwtmpv.widgets.cellview;

public interface SortComparator<T> {

  int compare(final T o1, final T o2, int multiplier);

}
