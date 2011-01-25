package org.gwtmpv.widgets.cellview;

import static org.gwtmpv.widgets.cellview.Cells.newClickableTextCell;
import static org.gwtmpv.widgets.cellview.Cells.newCompositeHeader;
import static org.gwtmpv.widgets.cellview.Cells.newHeader;
import static org.gwtmpv.widgets.cellview.Cells.newTextCell;
import static org.gwtmpv.widgets.cellview.Cells.newTextHeader;

/** A header that can sort, all done client-side. */
public class SortHeader<T> extends DelegateIsHeader<Object> {

  /** References to all of the headers to mark-as-not-sorted the others when we sort. */
  private final SortHeaders<T> headers;
  private final String name;
  private final SortComparator<T> comparator;
  protected Sorted sorted = Sorted.NO;

  public <C extends Comparable<C>> SortHeader(final SortHeaders<T> headers, final String name, final ColumnValue<T, C> columnValue) {
    this(headers, name, new SortComparator<T>() {
      public int compare(final T o1, final T o2, int multiplier) {
        final C v1 = columnValue.get(o1);
        final C v2 = columnValue.get(o2);
        return (v1 == null ? (v2 == null ? 0 : v2.compareTo(v1)) : v1.compareTo(v2)) * multiplier;
      }
    });
  }

  public SortHeader(final SortHeaders<T> headers, final String name, SortComparator<T> comparator) {
    this(headers, new CompositeHeaderFactory() {
      public IsHeader<Object> newHeader(IsHeader<String> text, IsHeader<String> arrow) {
        return newCompositeHeader(text, newTextHeader(" "), arrow);
      }
    }, name, comparator);
  }

  public SortHeader(SortHeaders<T> headers, CompositeHeaderFactory factory, String name, SortComparator<T> comparator) {
    super.delegate = factory.newHeader(//
      newHeader(new SortHeaderValue(), newClickableTextCell()),//
      newHeader(new SortTextValue(), newTextCell()));
    this.headers = headers;
    this.name = name;
    this.comparator = comparator;
    headers.add(this);
  }

  /** Mark this column as already sorted by the server. */
  public SortHeader<T> isAlreadySorted() {
    sorted = Sorted.ASC;
    return this;
  }

  @Override
  public String getValue() {
    return name;
  }

  public void unsort() {
    sorted = Sorted.NO;
  }

  public void toggle() {
    sorted = sorted.toggle();
  }

  public void triggerSort() {
    headers.resortTable(SortHeader.this, comparator);
  }

  public Sorted getSorted() {
    return sorted;
  }

  /** When the header is clicked, sort/resort the table based on this column. */
  private final class SortHeaderValue extends HeaderValue<String> {
    public String get() {
      return name;
    }

    public void set(final String value) {
      triggerSort();
    }
  }

  private final class SortTextValue extends HeaderValue<String> {
    public String get() {
      return sorted.icon();
    }
  }

  /** Allows clients to create a custom composite header based on the initial text/arrow headers. */
  public interface CompositeHeaderFactory {
    IsHeader<Object> newHeader(IsHeader<String> text, IsHeader<String> arrow);
  }

}
