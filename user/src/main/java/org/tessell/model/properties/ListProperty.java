package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.integerProperty;

import java.util.ArrayList;

import org.tessell.model.events.ValueAddedEvent;
import org.tessell.model.events.ValueAddedHandler;
import org.tessell.model.events.ValueRemovedEvent;
import org.tessell.model.events.ValueRemovedHandler;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;

import com.google.gwt.event.shared.HandlerRegistration;

public class ListProperty<E> extends AbstractProperty<ArrayList<E>, ListProperty<E>> {

  private IntegerProperty size;

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
