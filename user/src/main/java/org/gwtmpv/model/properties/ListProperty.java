package org.gwtmpv.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;

import java.util.ArrayList;

import org.gwtmpv.model.events.ValueAddedEvent;
import org.gwtmpv.model.events.ValueRemovedEvent;
import org.gwtmpv.model.events.ValueAddedEvent.ValueAddedHandler;
import org.gwtmpv.model.events.ValueRemovedEvent.ValueRemovedHandler;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.Value;

import com.google.gwt.event.shared.HandlerRegistration;

public class ListProperty<E> extends AbstractProperty<ArrayList<E>, ListProperty<E>> {

  public ListProperty(final Value<ArrayList<E>> value) {
    super(value);
  }

  // not sure why addExisting exists
  public void addExisting(final E item) {
    get().add(item);
    setTouched(true);
    fireEvent(new ValueAddedEvent<E>(this, item));
    lastValue = null; // force changed
    reassess();
  }

  public void add(final E item) {
    addExisting(item);
  }

  public void remove(final E item) {
    if (get().remove(item)) {
      fireEvent(new ValueRemovedEvent<E>(this, item));
      lastValue = null; // force changed
      reassess();
    }
  }

  public void clear() {
    final int size = get().size();
    for (int i = size - 1; i >= 0; i--) {
      final E value = get().remove(i);
      fireEvent(new ValueRemovedEvent<E>(this, value));
    }
    lastValue = null; // force changed
    reassess();
  }

  public IntegerProperty size() {
    return addDerived(integerProperty(new DerivedValue<Integer>() {
      public Integer get() {
        final ArrayList<E> current = ListProperty.this.get();
        return (current == null) ? null : current.size();
      }
    }));
  }

  public HandlerRegistration addValueAddedHandler(final ValueAddedHandler<E> handler) {
    return handlers.addHandler(ValueAddedEvent.getType(), handler);
  }

  public HandlerRegistration addValueRemovedHandler(final ValueRemovedHandler<E> handler) {
    return handlers.addHandler(ValueRemovedEvent.getType(), handler);
  }

  @Override
  protected ListProperty<E> getThis() {
    return this;
  }

}