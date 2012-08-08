package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.ArrayList;
import java.util.Collection;

import org.tessell.model.events.ValueAddedEvent;
import org.tessell.model.events.ValueAddedHandler;
import org.tessell.model.events.ValueRemovedEvent;
import org.tessell.model.events.ValueRemovedHandler;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;
import org.tessell.util.MapToList;

import com.google.gwt.event.shared.HandlerRegistration;

public class ListProperty<E> extends AbstractProperty<ArrayList<E>, ListProperty<E>> {

  private IntegerProperty size;

  /** Used to convert a list from one type of element to another. */
  public interface ElementConverter<E, F> {
    F to(E element);

    E from(F element);
  }

  public ListProperty(final Value<ArrayList<E>> value) {
    super(value);
  }

  @Override
  public void set(final ArrayList<E> items) {
    ArrayList<E> added = diff(get(), items);
    ArrayList<E> removed = diff(items, get());
    super.set(items);
    for (E add : added) {
      fireEvent(new ValueAddedEvent<E>(this, add));
    }
    for (E remove : removed) {
      fireEvent(new ValueRemovedEvent<E>(this, remove));
    }
  }

  /** Adds {@code item}, firing a {@link ValueAddedEvent}. */
  public void add(final E item) {
    get().add(item);
    setTouched(true);
    fireEvent(new ValueAddedEvent<E>(this, item));
    // will fire change if needed
    reassess();
  }

  /** Adds each item in {@code items}, firing a {@link ValueAddedEvent} for each. */
  public void addAll(Collection<? extends E> items) {
    if (items.size() == 0) {
      return; // this makes sense, right?
    }
    setTouched(true);
    for (E item : items) {
      get().add(item);
      fireEvent(new ValueAddedEvent<E>(this, item));
    }
    // will fire change if needed
    reassess();
  }

  /** Removes {@code item}, firing a {@link ValueRemovedEvent}. */
  public void remove(final E item) {
    // should be considered touched?
    if (get().remove(item)) {
      fireEvent(new ValueRemovedEvent<E>(this, item));
      reassess();
    }
  }

  /** Removes all entries, firing a {@link ValueRemovedEvent} for each. */
  public void clear() {
    final int size = get().size();
    for (int i = size - 1; i >= 0; i--) {
      final E value = get().remove(i);
      fireEvent(new ValueRemovedEvent<E>(this, value));
    }
    reassess();
  }

  /** @return a derived property that reflects this list's size. */
  public IntegerProperty size() {
    if (size == null) {
      size = addDerived(integerProperty(new DerivedValue<Integer>() {
        public Integer get() {
          final ArrayList<E> current = ListProperty.this.get();
          return (current == null) ? null : current.size();
        }
      }));
    }
    return size;
  }

  /** Registers {@code handler} to be called when new values are added. */
  public HandlerRegistration addValueAddedHandler(final ValueAddedHandler<E> handler) {
    return addHandler(ValueAddedEvent.getType(), handler);
  }

  /** Registers {@code handler} to be called when values are removed. */
  public HandlerRegistration addValueRemovedHandler(final ValueRemovedHandler<E> handler) {
    return addHandler(ValueRemovedEvent.getType(), handler);
  }

  /**
   * Creates a new {@link ListProperty>} of type {@code F}.
   *
   * Any changes made to either list will be reflected in the other,
   * using {@code converter} to go between {@code E} and {@code F}.
   */
  public <F> ListProperty<F> as(final ElementConverter<E, F> converter) {
    final MapToList<E, F> eToF = new MapToList<E, F>();
    final MapToList<F, E> fToE = new MapToList<F, E>();
    // make an intial copy of all the elements currently in our list
    ArrayList<F> initial = new ArrayList<F>();
    for (E e : get()) {
      F f = converter.to(e);
      eToF.add(e, f);
      fToE.add(f, e);
      initial.add(f);
    }
    final ListProperty<F> as = listProperty(getName(), initial);
    final boolean[] active = { false };
    // keep converting E -> F into as
    addValueAddedHandler(new ValueAddedHandler<E>() {
      public void onValueAdded(ValueAddedEvent<E> event) {
        if (!active[0]) {
          active[0] = true;
          E e = event.getValue();
          F f = converter.to(e);
          eToF.add(e, f);
          fToE.add(f, e);
          as.add(f);
          active[0] = false;
        }
      }
    });
    // remove Fs as Es are removed
    addValueRemovedHandler(new ValueRemovedHandler<E>() {
      public void onValueRemoved(ValueRemovedEvent<E> event) {
        if (!active[0]) {
          active[0] = true;
          E e = event.getValue();
          F f = eToF.removeOne(e);
          fToE.removeOne(f);
          as.remove(f);
          active[0] = false;
        }
      }
    });
    // also convert new Fs back into Es
    as.addValueAddedHandler(new ValueAddedHandler<F>() {
      public void onValueAdded(ValueAddedEvent<F> event) {
        if (!active[0]) {
          active[0] = true;
          F f = event.getValue();
          E e = converter.from(f);
          fToE.add(f, e);
          eToF.add(e, f);
          add(e);
          active[0] = false;
        }
      }
    });
    // and remove Es as Fs are removed
    as.addValueRemovedHandler(new ValueRemovedHandler<F>() {
      public void onValueRemoved(ValueRemovedEvent<F> event) {
        if (!active[0]) {
          active[0] = true;
          F f = event.getValue();
          E e = fToE.removeOne(f);
          eToF.removeOne(e);
          remove(e);
          active[0] = false;
        }
      }
    });
    return as;
  }

  @Override
  protected ListProperty<E> getThis() {
    return this;
  }

  @Override
  protected ArrayList<E> copyLastValue(ArrayList<E> newValue) {
    if (newValue == null) {
      return null;
    }
    return new ArrayList<E>(newValue);
  }

  /** @return the elements in two that are not in one */
  private static <E> ArrayList<E> diff(ArrayList<E> one, ArrayList<E> two) {
    ArrayList<E> diff = new ArrayList<E>();
    if (one == null || two == null) {
      return diff;
    }
    for (E t : two) {
      if (!one.contains(t)) {
        diff.add(t);
      }
    }
    return diff;
  }

}
