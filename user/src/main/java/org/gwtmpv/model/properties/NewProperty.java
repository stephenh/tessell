package org.gwtmpv.model.properties;

import java.util.ArrayList;

import org.bindgen.Binding;
import org.gwtmpv.model.Dto;
import org.gwtmpv.model.Model;
import org.gwtmpv.model.values.BoundValue;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.SetValue;
import org.gwtmpv.model.values.Value;

/** Lots of helper methods to constructor {@link Property}s out of bindings/{@link DerivedValue}s/etc. */
public class NewProperty {

  public static <P> BasicProperty<P> property(String name, P value) {
    return new BasicProperty<P>(new SetValue<P>(name, value));
  }

  public static <P> BasicProperty<P> derivedProperty(final DerivedValue<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> basicProperty(final Binding<P> binding) {
    return new BasicProperty<P>(new BoundValue<P>(binding));
  }

  public static BooleanProperty booleanProperty(final String name) {
    return new BooleanProperty(new SetValue<Boolean>(name));
  }

  public static BooleanProperty booleanProperty(final String name, final boolean initialValue) {
    return new BooleanProperty(new SetValue<Boolean>(name, initialValue));
  }

  public static BooleanProperty booleanProperty(final Value<Boolean> value) {
    return new BooleanProperty(value);
  }

  public static BooleanProperty booleanProperty(final Binding<Boolean> binding) {
    return new BooleanProperty(new BoundValue<Boolean>(binding));
  }

  public static IntegerProperty integerProperty(final String name) {
    return new IntegerProperty(new SetValue<Integer>(name));
  }

  public static IntegerProperty integerProperty(final String name, Integer i) {
    return new IntegerProperty(new SetValue<Integer>(name, i));
  }

  public static IntegerProperty integerProperty(final Value<Integer> derived) {
    return new IntegerProperty(derived);
  }

  public static IntegerProperty integerProperty(final Binding<Integer> binding) {
    return new IntegerProperty(new BoundValue<Integer>(binding));
  }

  public static StringProperty stringProperty(final String name) {
    return new StringProperty(new SetValue<String>(name));
  }

  public static StringProperty stringProperty(final String name, final String initialValue) {
    return new StringProperty(new SetValue<String>(name, initialValue));
  }

  public static StringProperty stringProperty(final Binding<String> binding) {
    return new StringProperty(new BoundValue<String>(binding));
  }

  public static StringProperty stringProperty(final Value<String> value) {
    return new StringProperty(value);
  }

  public static <E> ListProperty<E> listProperty(final Binding<ArrayList<E>> binding) {
    return new ListProperty<E>(new BoundValue<ArrayList<E>>(binding));
  }

  public static <E extends Model<F>, F extends Dto<E>> DtoListProperty<E, F> dtoListProperty(final Binding<ArrayList<F>> binding) {
    final SetValue<ArrayList<E>> modelListValue = new SetValue<ArrayList<E>>("modelList", new ArrayList<E>());
    return new DtoListProperty<E, F>(modelListValue, new BoundValue<ArrayList<F>>(binding));
  }

  public static <E> ListProperty<E> listProperty(final String name) {
    return new ListProperty<E>(new SetValue<ArrayList<E>>(name, new ArrayList<E>()));
  }

  public static <P> SetValue<P> setValue(String name) {
    return new SetValue<P>(name);
  }

  public static <P> SetValue<P> setValue(String name, P initialValue) {
    return new SetValue<P>(name, initialValue);
  }
}
