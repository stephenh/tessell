package org.tessell.widgets.cellview;

import org.bindgen.BindingRoot;
import org.tessell.model.properties.Property;

/**
 * Binds {@link Property}s to columns.
 *
 * This way you can have a {@code Property<String>} and create a column
 * with a {@code Cell<String>} (instead of having to make all new
 * {@code Cell<Property<String>> cells.
 * 
 * E.g.:
 * <code>
 *     // Foo.name is a StringProperty, e.g. Foo is rich model object
 *     FooBinding b = new FooBinding();
 *     t.addColumn(newColumn(boundProperty(b.name()), newTextCell()));
 * </code>
 */
public class BoundColumnProperty<T, C> extends ColumnValue<T, C> {

  private final BindingRoot<T, ? extends Property<C>> binding;

  public BoundColumnProperty(BindingRoot<T, ? extends Property<C>> binding) {
    this.binding = binding;
  }

  @Override
  public C get(T object) {
    return binding.getWithRoot(object).get();
  }

  @Override
  public void set(T object, C value) {
    binding.getWithRoot(object).set(value);
  }

}
