package org.gwtmpv.widgets.cellview;

import org.bindgen.BindingRoot;
import org.gwtmpv.util.UserStringable;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

public class BoundColumn<T, C> extends Column<T, C> {

  private final BindingRoot<T, C> binding;

  public static <R, P> BoundColumn<R, P> of(final BindingRoot<R, P> binding, final Cell<P> cell) {
    return new BoundColumn<R, P>(binding, cell);
  }

  public static <R> BoundColumn<R, String> ofString(final BindingRoot<R, String> binding) {
    return new BoundColumn<R, String>(binding, new AbstractCell<String>() {
      @Override
      public void render(final String value, final Object viewData, final StringBuilder sb) {
        if (value != null) {
          sb.append(value);
        }
      }
    });
  }

  public static <R, P extends UserStringable> BoundColumn<R, P> ofUserStringable(final BindingRoot<R, P> binding) {
    return new BoundColumn<R, P>(binding, new AbstractCell<P>() {
      @Override
      public void render(final P value, final Object viewData, final StringBuilder sb) {
        if (value != null) {
          sb.append(value.toUserString());
        }
      }
    });
  }

  public BoundColumn(final BindingRoot<T, C> binding, final Cell<C> cell) {
    super(cell);
    this.binding = binding;
  }

  @Override
  public C getValue(final T row) {
    return binding.getWithRoot(row);
  }

}
