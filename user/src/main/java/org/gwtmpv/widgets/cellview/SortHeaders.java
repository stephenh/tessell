package org.gwtmpv.widgets.cellview;

import static org.gwtmpv.widgets.cellview.Cells.boundValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bindgen.BindingRoot;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.widgets.IsCellTable;

/** A collection for all of the sort headers in a table so they can unsort each other as needed. */
public class SortHeaders<T> {

  protected final IsCellTable<T> cellTable;
  protected final ArrayList<SortHeader<T>> all = new ArrayList<SortHeader<T>>();

  public SortHeaders(final IsCellTable<T> cellTable) {
    this.cellTable = cellTable;
  }

  /** @return a new sort header for a given name, sorted by {@code binding}. */
  public <U extends Comparable<U>> SortHeader<T> on(final String name, final BindingRoot<T, U> binding) {
    return new SortHeader<T>(this, name, boundValue(binding));
  }

  /** @return a new sort header for a given name, sorted by {@code columnValue}. */
  public <C extends Comparable<C>> SortHeader<T> on(final String name, final ColumnValue<T, C> columnValue) {
    return new SortHeader<T>(this, name, columnValue);
  }

  /** @return a new sort header for a given name, sorted by {@code comparator}. */
  public SortHeader<T> on(final String name, final SortComparator<T> comparator) {
    return new SortHeader<T>(this, name, comparator);
  }

  /** @return a new sort header for a given name, sorted by {@code binding}. */
  public <U extends Comparable<U>> SortHeader<T> on(final BindingRoot<T, U> binding) {
    return new SortHeader<T>(this, Inflector.humanize(binding.getName()), boundValue(binding));
  }

  public void add(SortHeader<T> header) {
    all.add(header);
  }

  public void resortTable(final SortHeader<T> on, final SortComparator<T> c) {
    // update the sorted state of all the column
    for (final SortHeader<T> other : all) {
      if (other == on) {
        on.toggle();
      } else {
        other.unsort();
      }
    }
    // Assume we're only sorted the data we've got here on the screen
    final List<T> data = new ArrayList<T>(cellTable.getDisplayedItems());
    Collections.sort(data, new Comparator<T>() {
      public int compare(T o1, T o2) {
        return c.compare(o1, o2, on.sorted.offset());
      }
    });
    cellTable.setRowData(data);
    cellTable.redrawHeaders();
  }

}
