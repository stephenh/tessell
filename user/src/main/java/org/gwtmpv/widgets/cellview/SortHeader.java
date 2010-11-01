package org.gwtmpv.widgets.cellview;

import java.util.Comparator;

import org.bindgen.BindingRoot;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/** A header that can sort, all done client-side. */
public class SortHeader<T, U extends Comparable<U>> extends ExposedUpdaterHeader<String> {

  private final SortHeaders<T> headers;
  private final String name;
  private final BindingRoot<T, U> binding;
  protected Sorted sorted = Sorted.NO;
  private final Comparator<T> c = new Comparator<T>() {
    @Override
    public int compare(final T o1, final T o2) {
      final U v1 = binding.getWithRoot(o1);
      final U v2 = binding.getWithRoot(o2);
      return v1.compareTo(v2) * sorted.offset();
    }
  };

  public SortHeader(final SortHeaders<T> headers, final String name, final BindingRoot<T, U> binding) {
    super(new ClickableTextCell());
    this.headers = headers;
    this.name = name;
    this.binding = binding;
    setUpdater(new OnValueUpdated());
    headers.add(this);
  }

  /** Mark this column as already sorted by the server. */
  public SortHeader<T, U> isAlreadySorted() {
    sorted = Sorted.ASC;
    return this;
  }

  @Override
  public String getValue() {
    return name;
  }

  @Override
  public void render(final SafeHtmlBuilder sb) {
    super.render(sb);
    sb.appendHtmlConstant("&nbsp;");
    sb.appendHtmlConstant(sorted.icon());
  }

  public void unsort() {
    sorted = Sorted.NO;
  }

  public void toggle() {
    sorted = sorted.toggle();
  }

  /** When the header is clicked, sort/resort the table based on this column. */
  private final class OnValueUpdated implements ValueUpdater<String> {
    @Override
    public void update(final String value) {
      headers.resortTable(SortHeader.this, c);
    }
  }

}
