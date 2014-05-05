package org.tessell.model.properties;

import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;

import org.bindgen.Binding;
import org.tessell.model.values.BoundValue;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.model.values.Value;

/** Lots of helper methods to constructor {@link Property}s out of bindings/{@link DerivedValue}s/etc. */
public class NewProperty {

  public static <P> BasicProperty<P> property(String name, P value) {
    return new BasicProperty<P>(new SetValue<P>(name, value));
  }

  public static <P> BasicProperty<P> derivedProperty(final DerivedValue<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> basicProperty(final Value<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> basicProperty(final Binding<P> binding) {
    return new BasicProperty<P>(new BoundValue<P>(binding));
  }

  public static <P> BasicProperty<P> basicProperty(final String name) {
    return new BasicProperty<P>(new SetValue<P>(name));
  }

  public static <P> BasicProperty<P> basicProperty(final String name, P initialValue) {
    return new BasicProperty<P>(new SetValue<P>(name, initialValue));
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

  public static Property<Boolean> not(final Property<Boolean> property) {
    return property.formatted(new PropertyFormatter<Boolean, Boolean>() {
      public Boolean format(Boolean a) {
        return !property.get();
      }

      public Boolean parse(Boolean b) throws Exception {
        return !b;
      }
    });
  }

  @SafeVarargs
  public static BooleanProperty or(final Property<Boolean>... properties) {
    return booleanProperty(new DerivedValue<Boolean>() {
      public Boolean get() {
        for (Property<Boolean> property : properties) {
          if (TRUE.equals(property.get())) {
            return true;
          }
        }
        return false;
      }
    });
  }

  @SafeVarargs
  public static BooleanProperty and(final Property<Boolean>... properties) {
    return booleanProperty(new DerivedValue<Boolean>() {
      public Boolean get() {
        for (Property<Boolean> property : properties) {
          if (!TRUE.equals(property.get())) {
            return false;
          }
        }
        return true;
      }
    });
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

  public static LongProperty longProperty(final String name) {
    return new LongProperty(new SetValue<Long>(name));
  }

  public static LongProperty longProperty(final String name, Long i) {
    return new LongProperty(new SetValue<Long>(name, i));
  }

  public static LongProperty longProperty(final Value<Long> derived) {
    return new LongProperty(derived);
  }

  public static LongProperty longProperty(final Binding<Long> binding) {
    return new LongProperty(new BoundValue<Long>(binding));
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

  public static <E> ListProperty<E> listProperty(final Value<List<E>> value) {
    return new ListProperty<E>(value);
  }

  public static <E> ListProperty<E> listProperty(final Binding<List<E>> binding) {
    return new ListProperty<E>(new BoundValue<List<E>>(binding));
  }

  public static <E> ListProperty<E> listProperty(final String name, final List<E> list) {
    return new ListProperty<E>(new SetValue<List<E>>(name, list));
  }

  public static <E> ListProperty<E> listProperty(final String name) {
    return new ListProperty<E>(new SetValue<List<E>>(name, new ArrayList<E>()));
  }

  public static <P> SetValue<P> setValue(String name) {
    return new SetValue<P>(name);
  }

  public static <P> SetValue<P> setValue(String name, P initialValue) {
    return new SetValue<P>(name, initialValue);
  }

  public static <E extends Enum<E>> EnumProperty<E> enumProperty(final Value<E> value) {
    return new EnumProperty<E>(value);
  }

  public static <E extends Enum<E>> EnumProperty<E> enumProperty(final String name) {
    return new EnumProperty<E>(new SetValue<E>(name));
  }

  public static <E extends Enum<E>> EnumProperty<E> enumProperty(final String name, E initialValue) {
    return new EnumProperty<E>(new SetValue<E>(name, initialValue));
  }

  public static <E extends Enum<E>> EnumProperty<E> enumProperty(Binding<E> binding) {
    return new EnumProperty<E>(new BoundValue<E>(binding));
  }

}
