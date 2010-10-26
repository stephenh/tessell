package org.gwtmpv.model.values;

import org.gwtmpv.model.properties.AbstractProperty;
import org.gwtmpv.model.properties.FormattedProperty;

/**
 * Pretends to be a value, but delegates to {@link DelegatedValue.Delegate}. 
 * 
 * This currently an ugly hack for {@link FormattedProperty} because it wants
 * to pass an essentially derived {@link Value} to the {@link AbstractProperty}
 * super constructor, but have the {@code Value} be derived from the
 * {@code FormattedProperty} itself.
 *
 * Due to Java constructor/field initialization limitations, an inner class
 * (anonymous or otherwise) that refers to {@code this} cannot be passed to
 * the super constructor because {@code this} has not been initialized.
 *
 * So, we pass {@code DelegatedValue} instead, and after {@code FormattedProperty}
 * is properly constructed, call {@link #setDelegate(Delegate)}.
 *
 * @param <V> the value type
 */
public class DelegatedValue<V> implements Value<V> {

  public static interface Delegate<V> {
    V valueGet();

    void valueSet(V value);

    String valueName();
  }

  private Delegate<V> delegate;

  @Override
  public V get() {
    return (delegate != null) ? delegate.valueGet() : null;
  }

  @Override
  public void set(V v) {
    if (delegate != null) {
      delegate.valueSet(v);
    }
  }

  @Override
  public String getName() {
    return (delegate != null) ? delegate.valueName() : null;
  }

  public void setDelegate(Delegate<V> delegate) {
    this.delegate = delegate;
  }

}
