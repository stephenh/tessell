package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.basicProperty;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.util.ObjectUtils.eq;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.values.DerivedValue;

/** Methods that can be used cross the value-based {@link AbstractProperty} and also derived properties like {@link FormattedProperty} and {@link ConvertedProperty}. */
abstract class AbstractAbstractProperty<P> implements Property<P> {

  /** Track {@code other} as derived on us, so we'll forward changed/changing events to it. */
  @Override
  public <P1 extends Property<?>> P1 addDerived(final P1 other) {
    return addDerived(other, this, true);
  }

  /** Remove {@code other} as derived on us. */
  @Override
  public <P1 extends Property<?>> P1 removeDerived(final P1 other) {
    return removeDerived(other, this);
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  @Override
  public <T1> FormattedProperty<T1, P> formatted(final PropertyFormatter<P, T1> formatter) {
    return new FormattedProperty<T1, P>(this, formatter);
  }

  /** @return a new derived property by applying {@code formatter} to our value */
  @Override
  public <T1> FormattedProperty<T1, P> formatted(final String invalidMessage, final PropertyFormatter<P, T1> formatter) {
    return new FormattedProperty<T1, P>(this, formatter, invalidMessage);
  }

  /** @return a new derived property by applying {@code converter} to our value */
  @Override
  public <T1> Property<T1> as(final PropertyConverter<P, T1> converter) {
    return new ConvertedProperty<T1, P>(this, converter);
  }

  @Override
  public Property<String> asString() {
    return as(new PropertyConverter<P, String>() {
      public String to(P a) {
        return a.toString();
      }
    });
  }

  @Override
  public Property<Boolean> is(final P value) {
    return is(value, null);
  }

  @Override
  public Property<Boolean> is(final P value, final P whenUnsetValue) {
    final BooleanProperty is = booleanProperty(getName() + "Is" + value);
    is.setInitialValue(isEqual(get(), value));
    final boolean[] changing = { false };
    // is -> this
    is.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        if (isReadOnly()) {
          changing[0] = true;
          is.set(isEqual(get(), value));
          changing[0] = false;
        } else if (event.getNewValue() != null && event.getNewValue()) {
          AbstractAbstractProperty.this.set(value);
        } else if (!changing[0]) {
          AbstractAbstractProperty.this.set(whenUnsetValue);
        }
      }
    });
    // this -> is
    addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(isEqual(get(), value));
        changing[0] = false;
      }
    });
    return is;
  }

  @Override
  public Property<Boolean> is(final Property<P> other) {
    return is(other, null);
  }

  @Override
  public Property<Boolean> is(final Property<P> other, final P whenUnsetValue) {
    final BooleanProperty is = booleanProperty(getName() + "Is" + other.getName());
    is.setInitialValue(isEqual(get(), other.get()));
    final boolean[] changing = { false };
    // is -> this
    is.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        if (isReadOnly()) {
          changing[0] = true;
          is.set(isEqual(get(), other.get()));
          changing[0] = false;
        } else if (event.getNewValue() != null && event.getNewValue()) {
          AbstractAbstractProperty.this.set(other.get());
        } else if (!changing[0]) {
          AbstractAbstractProperty.this.set(whenUnsetValue);
        }
      }
    });
    // this -> is
    addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(isEqual(get(), other.get()));
        changing[0] = false;
      }
    });
    // other -> is
    other.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        changing[0] = true;
        is.set(isEqual(get(), other.get()));
        changing[0] = false;
      }
    });
    return is;
  }

  @Override
  public Property<P> orIfNull(final P ifNullValue) {
    return basicProperty(new DerivedValue<P>(getName()) {
      public P get() {
        P thisValue = AbstractAbstractProperty.this.get();
        return thisValue == null ? ifNullValue : thisValue;
      }
    });
  }

  /** Checks equality between a and b for the {@link #is} methods. Overrideable by subclasses. */
  protected boolean isEqual(P a, P b) {
    return eq(a, b);
  }

}
