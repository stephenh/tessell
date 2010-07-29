package org.gwtmpv.widgets.cellview;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bindgen.BindingRoot;
import org.gwtmpv.widgets.IsCellTable;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class SortHeader<T, U extends Comparable<U>> extends Header<String> {

  private final IsCellTable<T> table;
  private final String name;
  private final BindingRoot<T, U> binding;
  private boolean isReverse = true;

  public static <T, U extends Comparable<U>> SortHeader<T, U> on(final IsCellTable<T> table, final String name,
                                                                 final BindingRoot<T, U> binding) {
    return new SortHeader<T, U>(table, name, binding);
  }

  public SortHeader(final IsCellTable<T> table, final String name, final BindingRoot<T, U> binding) {
    super(new ClickableTextCell());
    this.table = table;
    this.name = name;
    this.binding = binding;
    setUpdater(new OnValueUpdated());
  }

  public SortHeader<T, U> isAlreadySorted() {
    this.isReverse = false;
    return this;
  }

  @Override
  public String getValue() {
    return name;
  }

  private final class OnValueUpdated implements ValueUpdater<String> {
    @Override
    public void update(final String value) {
      isReverse = !isReverse;
      final Comparator<T> c = new Comparator<T>() {
        @Override
        public int compare(final T o1, final T o2) {
          final U v1 = binding.getWithRoot(o1);
          final U v2 = binding.getWithRoot(o2);
          return v1.compareTo(v2) * (isReverse ? -1 : 1);
        }
      };
      final List<T> data = table.getDisplayedItems();
      Collections.sort(data, c);
      table.setData(0, data.size(), data);
      table.refreshHeaders();
    }
  }

}