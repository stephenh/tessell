package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.tessell.model.events.ValueAddedEvent;
import org.tessell.model.events.ValueAddedHandler;
import org.tessell.model.events.ValueRemovedEvent;
import org.tessell.model.events.ValueRemovedHandler;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;
import org.tessell.util.MapToList;

import com.google.gwt.event.shared.HandlerRegistration;

public class ListProperty<E> extends AbstractProperty<List<E>, ListProperty<E>> {

  private IntegerProperty size;
  private List<E> readOnly;
  private List<E> readOnlySource;

  /** Used to convert a list from one type of element to another. */
  public interface ElementConverter<E, F> {
    F to(E element);

    E from(F element);
  }

  @SuppressWarnings("unchecked")
  public ListProperty(final Value<? extends List<E>> value) {
    // the "? extends List<E>" is so we can be called with Value<ArrayList<E>>
    // types, which dtonator currently generates in the value inner classes
    super((Value<List<E>>) value);
  }

  @Override
  public List<E> get() {
    List<E> current = super.get();
    // change the wrapped list only when the source identity changes
    if (readOnly == null || current != readOnlySource) {
      readOnly = current == null ? null : Collections.unmodifiableList(current);
      readOnlySource = current;
    }
    return readOnly;
  }

  /** @return a copy of our list as an {@link ArrayList}, e.g. for GWT-RPC calls. */
  public ArrayList<E> getArrayList() {
    return new ArrayList<E>(getDirect());
  }

  @Override
  public void set(final List<E> items) {
    List<E> added = diff(getDirect(), items);
    List<E> removed = diff(items, getDirect());
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
    getDirect().add(item);
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
      getDirect().add(item);
      fireEvent(new ValueAddedEvent<E>(this, item));
    }
    // will fire change if needed
    reassess();
  }

  /** Removes {@code item}, firing a {@link ValueRemovedEvent}. */
  public void remove(final E item) {
    // should be considered touched?
    if (getDirect().remove(item)) {
      fireEvent(new ValueRemovedEvent<E>(this, item));
      reassess();
    }
  }

  /** Removes all entries, firing a {@link ValueRemovedEvent} for each. */
  public void clear() {
    final int size = getDirect().size();
    for (int i = size - 1; i >= 0; i--) {
      final E value = getDirect().remove(i);
      fireEvent(new ValueRemovedEvent<E>(this, value));
    }
    reassess();
  }

  /** @return a derived property that reflects this list's size. */
  public IntegerProperty size() {
    if (size == null) {
      size = addDerived(integerProperty(new DerivedValue<Integer>() {
        public Integer get() {
          final List<E> current = ListProperty.this.get();
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
    List<F> initial = new ArrayList<F>();
    if (get() != null) {
      for (E e : get()) {
        F f = converter.to(e);
        eToF.add(e, f);
        fToE.add(f, e);
        initial.add(f);
      }
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
  protected List<E> copyLastValue(List<E> newValue) {
    if (newValue == null) {
      return null;
    }
    return new ArrayList<E>(newValue);
  }

  private List<E> getDirect() {
    return super.get();
  }

  /** @return the elements in two that are not in one */
  private static <E> List<E> diff(List<E> one, List<E> two) {
    List<E> diff = new ArrayList<E>();
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
