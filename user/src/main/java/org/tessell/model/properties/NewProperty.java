package org.tessell.model.properties;

import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;

import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.LambdaValue;
import org.tessell.model.values.SetValue;
import org.tessell.model.values.Value;

import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;

/** Lots of helper methods to constructor {@link Property}s out of bindings/{@link DerivedValue}s/etc. */
public class NewProperty {

  public static <P> BasicProperty<P> property(String name, P value) {
    return new BasicProperty<P>(new SetValue<P>(name, value));
  }

  public static <P> BasicProperty<P> derivedProperty(final LambdaValue<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> derivedProperty(final DerivedValue<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> basicProperty(final Value<P> value) {
    return new BasicProperty<P>(value);
  }

  public static <P> BasicProperty<P> basicProperty(final String name) {
    return new BasicProperty<P>(new SetValue<P>(name));
  }

  public static <P> BasicProperty<P> basicProperty(final String name, P initialValue) {
    return new BasicProperty<P>(new SetValue<P>(name, initialValue));
  }

  public static <T> BasicProperty<T> basicProperty(final String name, final Getter<T> getter, Setter<T> setter) {
    return new BasicProperty<T>(new GetSetValue<T>(name, getter, setter));
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

  public static BooleanProperty booleanProperty(final String name, final Getter<Boolean> getter, Setter<Boolean> setter) {
    return new BooleanProperty(new GetSetValue<Boolean>(name, getter, setter));
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
  public static <E> ListProperty<E> union(final ListProperty<E>... properties) {
    return listProperty(new DerivedValue<List<E>>("union") {
      public List<E> get() {
        List<E> copy = new ArrayList<E>();
        for (ListProperty<E> property : properties) {
          copy.addAll(property.get());
        }
        return copy;
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

  /** @return a {@link BooleanProperty} of whether {@code draggable} is current being dragged over. */
  public static BooleanProperty draggingOver(HasAllDragAndDropHandlers draggable) {
    BooleanProperty over = booleanProperty("over");
    // for ignoring drag enters when we're selected
    boolean[] current = { false };
    draggable.addDragStartHandler(e -> current[0] = true);
    draggable.addDragEndHandler(e -> current[0] = false);
    // our children will bubble up dragEnters/dragLeaves as well, so keep track
    // of the "last" drag enter, which will be ours, and ignore them until then
    int[] count = { 0 };
    draggable.addDragEnterHandler(e -> {
      if (!current[0] && ++count[0] == 1) {
        over.set(true);
      }
    });
    draggable.addDragLeaveHandler(e -> {
      if (over.isTrue() && --count[0] == 0) {
        over.set(false);
      }
    });
    draggable.addDropHandler(e -> {
      over.set(false);
      count[0] = 0;
    });
    return over;
  }

  public static BooleanProperty dragging(HasAllDragAndDropHandlers draggable) {
    BooleanProperty dragging = booleanProperty("dragging");
    draggable.addDragStartHandler(e -> dragging.set(true));
    draggable.addDragEndHandler(e -> dragging.set(false));
    return dragging;
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

  public static IntegerProperty integerProperty(final String name, final Getter<Integer> getter, Setter<Integer> setter) {
    return new IntegerProperty(new GetSetValue<Integer>(name, getter, setter));
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

  public static LongProperty longProperty(final String name, final Getter<Long> getter, Setter<Long> setter) {
    return new LongProperty(new GetSetValue<Long>(name, getter, setter));
  }

  public static StringProperty stringProperty(final String name) {
    return new StringProperty(new SetValue<String>(name));
  }

  public static StringProperty stringProperty(final String name, final String initialValue) {
    return new StringProperty(new SetValue<String>(name, initialValue));
  }

  public static StringProperty stringProperty(final Value<String> value) {
    return new StringProperty(value);
  }

  public static StringProperty stringProperty(final String name, final Getter<String> getter, Setter<String> setter) {
    return new StringProperty(new GetSetValue<String>(name, getter, setter));
  }

  public static <E> ListProperty<E> listProperty(final Value<List<E>> value) {
    return new ListProperty<E>(value);
  }

  public static <E> ListProperty<E> listProperty(final String name, final List<E> list) {
    return new ListProperty<E>(new SetValue<List<E>>(name, list));
  }

  public static <E> ListProperty<E> listProperty(final String name) {
    return new ListProperty<E>(new SetValue<List<E>>(name, new ArrayList<E>()));
  }

  public static <E> ListProperty<E> listProperty(final String name, final Getter<List<E>> getter, Setter<List<E>> setter) {
    return new ListProperty<E>(new GetSetValue<List<E>>(name, getter, setter));
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

  /** Takes {@link Getter} and {@link Setter}, ideally provided as lambdas, and adapts them to a {@link Value}. */
  public static class GetSetValue<T> implements Value<T> {
    private final String name;
    private final Getter<T> getter;
    private final Setter<T> setter;

    GetSetValue(String name, Getter<T> getter, Setter<T> setter) {
      this.name = name;
      this.getter = getter;
      this.setter = setter;
    }

    @Override
    public T get() {
      return getter.get();
    }

    @Override
    public void set(T value) {
      setter.set(value);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isReadOnly() {
      return setter != null;
    }
  }

}
