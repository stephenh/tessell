package org.gwtmpv.model.properties;

import java.util.ArrayList;

import org.bindgen.Binding;
import org.gwtmpv.model.Dto;
import org.gwtmpv.model.Model;
import org.gwtmpv.model.values.BoundValue;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.SetValue;

/** Lots of helper methods to constructor {@link Property}s out of bindings/{@link DerivedValue}s/etc. */
public class NewProperty {

  public static <P> BasicProperty<P> property(String name, P value) {
    return new BasicProperty<P>(new SetValue<P>(name, value));
  }

  public static <P> BasicProperty<P> basicProperty(final Binding<P> binding) {
    return new BasicProperty<P>(new BoundValue<P>(binding));
  }

  public static BooleanProperty booleanProperty(final String name) {
    return new BooleanProperty(new SetValue<Boolean>(name));
  }

  public static BooleanProperty booleanProperty(final String name, final boolean initialValue) {
    final BooleanProperty b = booleanProperty(name);
    b.setInitial(initialValue);
    return b;
  }

  public static BooleanProperty booleanProperty(final DerivedValue<Boolean> value) {
    return new BooleanProperty(value);
  }

  public static BooleanProperty booleanProperty(final Binding<Boolean> binding) {
    return new BooleanProperty(new BoundValue<Boolean>(binding));
  }

  public static IntegerProperty integerProperty(final String name) {
    return new IntegerProperty(new SetValue<Integer>(name));
  }

  public static IntegerProperty integerProperty(final String name, Integer i) {
    SetValue<Integer> value = new SetValue<Integer>(name);
    value.set(i);
    return new IntegerProperty(value);
  }

  public static IntegerProperty integerProperty(final DerivedValue<Integer> derived) {
    return new IntegerProperty(derived);
  }

  public static IntegerProperty integerProperty(final Binding<Integer> binding) {
    return new IntegerProperty(new BoundValue<Integer>(binding));
  }

  public static StringProperty stringProperty(final String name) {
    return new StringProperty(new SetValue<String>(name));
  }

  public static StringProperty stringProperty(final String name, final String initialValue) {
    final StringProperty s = stringProperty(name);
    s.setInitial(initialValue);
    return s;
  }

  public static StringProperty stringProperty(final Binding<String> binding) {
    return new StringProperty(new BoundValue<String>(binding));
  }

  public static <E> ListProperty<E> listProperty(final Binding<ArrayList<E>> binding) {
    return new ListProperty<E>(new BoundValue<ArrayList<E>>(binding));
  }

  public static <E extends Model<F>, F extends Dto<E>> DtoListProperty<E, F> dtoListProperty(final Binding<ArrayList<F>> binding) {
    final SetValue<ArrayList<E>> modelListValue = new SetValue<ArrayList<E>>("modelList");
    modelListValue.set(new ArrayList<E>());
    return new DtoListProperty<E, F>(modelListValue, new BoundValue<ArrayList<F>>(binding));
  }

  public static <E> ListProperty<E> listProperty(final String name) {
    return new ListProperty<E>(new SetValue<ArrayList<E>>(name));
  }

}
