package org.gwtmpv.widgets.cellview;

import org.bindgen.BindingRoot;

/**
 * @param <T> the row type (root binding type in bindgen)
 * @param <C> the value type
 */
public class BoundColumnValue<T, C> extends ColumnValue<T, C> {

  private final BindingRoot<T, C> binding;

  public BoundColumnValue(final BindingRoot<T, C> binding) {
    this.binding = binding;
  }

  @Override
  public C get(final T row) {
    return binding.getWithRoot(row);
  }

  @Override
  public void set(T object, C value) {
    binding.setWithRoot(object, value);
  }

}
