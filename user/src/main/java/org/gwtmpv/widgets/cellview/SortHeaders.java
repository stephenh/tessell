package org.gwtmpv.widgets.cellview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bindgen.BindingRoot;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.widgets.IsCellTable;

/** A collection for all of the sort headers in a table so they can unsort each other as needed. */
public class SortHeaders<T> {

  private final IsCellTable<T> cellTable;
  private final ArrayList<SortHeader<T, ?>> all = new ArrayList<SortHeader<T, ?>>();

  public SortHeaders(final IsCellTable<T> cellTable) {
    this.cellTable = cellTable;
  }

  /** @return a new sort header for a given name, sorted by binding. */
  public <U extends Comparable<U>> SortHeader<T, U> on(final String name, final BindingRoot<T, U> binding) {
    return new SortHeader<T, U>(this, name, binding);
  }

  /** @return a new sort header for a given name, sorted by binding. */
  public <U extends Comparable<U>> SortHeader<T, U> on(final BindingRoot<T, U> binding) {
    return new SortHeader<T, U>(this, Inflector.humanize(binding.getName()), binding);
  }

  public void add(SortHeader<T, ?> header) {
    all.add(header);
  }

  public void resortTable(final SortHeader<T, ?> on, final Comparator<T> c) {
    // update the sorted state of all the column
    for (final SortHeader<T, ?> other : all) {
      if (other == on) {
        on.toggle();
      } else {
        other.unsort();
      }
    }
    // Assume we're only sorted the data we've got here on the screen
    final List<T> data = cellTable.getDisplayedItems();
    Collections.sort(data, c);
    cellTable.setRowData(0, data);
    cellTable.redrawHeaders();
  }

}
