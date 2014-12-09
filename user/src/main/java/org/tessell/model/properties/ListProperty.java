package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.derivedProperty;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.*;

import org.tessell.model.Model;
import org.tessell.model.events.*;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;
import org.tessell.util.ListDiff;
import org.tessell.util.ListDiff.Location;
import org.tessell.util.ObjectUtils;

import com.google.gwt.event.shared.HandlerRegistration;

public class ListProperty<E> extends AbstractProperty<List<E>, ListProperty<E>> implements HasMemberChangedHandlers {

  private IntegerProperty size;
  private BasicProperty<E> first;
  private BasicProperty<E> last;
  private List<E> readOnly;
  private List<E> readOnlySource;
  private Comparator<E> lastComparator;
  private Comparator<E> persistentComparator;
  private PropertyGroup allValid;

  /** Used to convert a list from one type of element to another. */
  public interface ElementConverter<E, F> {
    F to(E element);

    E from(F element);
  }

  /** Used to map a list from one type of element to another. */
  public interface ElementMapper<E, F> {
    F map(E element);
  }

  /** Used to filter a list to a matching condition. */
  public interface ElementFilter<E> {
    boolean matches(E element);
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

  @Override
  public Property<Boolean> is(final List<E> value) {
    return is(value, new ArrayList<E>());
  }

  @Override
  public Property<Boolean> is(final Property<List<E>> other) {
    return is(other, new ArrayList<E>());
  }

  /** @return a copy of our list as an {@link ArrayList}, e.g. for GWT-RPC calls. */
  public ArrayList<E> toArrayList() {
    return new ArrayList<E>(getDirect());
  }

  @Override
  public String toString() {
    List<E> e = getDirect();
    if (e == null) {
      return getValueObject().getName() + " null";
    }
    // Janky, but keep ListProperty.toString from being huge and accidentally ruining perf
    String s = getValueObject().getName() + " [";
    for (int i = 0; i < e.size() && i < 20; i++) {
      s += ObjectUtils.toStr(e.get(i), "null");
      if (i != e.size() - 1) {
        s += ", ";
      }
    }
    if (e.size() > 20) {
      s += "...";
    }
    s += "]";
    return s;
  }

  /** Adds {@code item}, firing a {@link ValueAddedEvent}. */
  public void add(final E item) {
    getDirect().add(item);
    sortIfNeeded();
    setTouched(true);
    // will fire add+change if needed
    reassess();
  }

  /** Adds {@code item}, firing a {@link ValueAddedEvent}. */
  public void add(final int index, final E item) {
    getDirect().add(index, item);
    sortIfNeeded();
    setTouched(true);
    // will fire add+change if needed
    reassess();
  }

  /** Adds each item in {@code items}, firing a {@link ValueAddedEvent} for each. */
  public void addAll(Collection<? extends E> items) {
    if (items.size() == 0) {
      return;
    }
    getDirect().addAll(items);
    sortIfNeeded();
    setTouched(true);
    // will fire adds+change if needed
    reassess();
  }

  /** Removes {@code item}, firing a {@link ValueRemovedEvent}. */
  public void remove(final E item) {
    getDirect().remove(item);
    sortIfNeeded();
    setTouched(true);
    // will fire remove+change if needed
    reassess();
  }

  /** Removes each item in {@code items}, firing a {@link ValueRemovedEvent} for each. */
  public void removeAll(Collection<? extends E> items) {
    if (items.size() == 0) {
      return;
    }
    getDirect().removeAll(items);
    setTouched(true);
    // will fire adds+change if needed
    reassess();
  }

  /** Removes all entries, firing a {@link ValueRemovedEvent} for each. */
  public void clear() {
    getDirect().clear();
    // will fire removes+change if needed
    reassess();
  }

  /** @return a derived property of whether {@code item} is in this list. */
  public BooleanProperty contains(final E item) {
    return addDerived(booleanProperty(new Value<Boolean>() {
      @Override
      public Boolean get() {
        final List<E> current = ListProperty.this.get();
        return (current == null) ? false : current.contains(item);
      }

      @Override
      public void set(Boolean value) {
        final List<E> current = ListProperty.this.get();
        if (current != null) {
          if (Boolean.TRUE.equals(value) && !current.contains(item)) {
            add(item);
          } else if (!Boolean.TRUE.equals(value) && current.contains(item)) {
            remove(item);
          }
        }
      }

      @Override
      public String getName() {
        return "contains " + item;
      }

      @Override
      public boolean isReadOnly() {
        return false;
      }
    }));
  }

  /** @return a two-way property of whether {@code items} are a subset of this list. */
  public BooleanProperty containsAll(final List<E> items) {
    final BooleanProperty b = booleanProperty(getName() + "ContainsAll", false);
    final boolean[] firing = { false };
    // conditionally update us when b goes true->false/false->true
    b.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(final PropertyChangedEvent<Boolean> event) {
        if (!firing[0]) {
          firing[0] = true;
          if (event.getNewValue()) {
            for (final E item : items) {
              if (!get().contains(item)) {
                add(item);
              }
            }
          } else {
            removeAll(items);
          }
          firing[0] = false;
        }
      }
    });
    // conditionally update b when we change
    addPropertyChangedHandler(new PropertyChangedHandler<List<E>>() {
      public void onPropertyChanged(final PropertyChangedEvent<List<E>> event) {
        if (!firing[0]) {
          firing[0] = true;
          b.set(get().containsAll(items));
          firing[0] = false;
        }
      }
    });
    return b;
  }

  /** @return a derived property that reflects this list's size. */
  public IntegerProperty size() {
    if (size == null) {
      size = addDerived(integerProperty(new DerivedValue<Integer>(getValueObject().getName() + "Size") {
        public Integer get() {
          final List<E> current = ListProperty.this.get();
          return (current == null) ? null : current.size();
        }
      }));
    }
    return size;
  }

  /** @return a derived property that reflects this list's first element (or null) */
  public Property<E> first() {
    if (first == null) {
      first = derivedProperty(new DerivedValue<E>("first") {
        public E get() {
          List<E> list = ListProperty.this.get();
          return list == null || list.isEmpty() ? null : list.get(0);
        }
      });
    }
    return first;
  }

  /** @return a derived property that reflects this list's last element (or null) */
  public Property<E> last() {
    if (last == null) {
      last = derivedProperty(new DerivedValue<E>("last") {
        public E get() {
          List<E> list = ListProperty.this.get();
          return list == null || list.isEmpty() ? null : list.get(list.size() - 1);
        }
      });
    }
    return last;
  }

  /** @return a property that will reflect whether {@code element} is the first element. */
  public BooleanProperty isFirst(final E element) {
    return booleanProperty(new DerivedValue<Boolean>(element + "IsFirst") {
      public Boolean get() {
        List<E> l = ListProperty.this.get();
        return l != null && !l.isEmpty() && element.equals(l.get(0));
      }
    });
  }

  /** Moves {@code element} up in the list; noop if it's already first. */
  public void moveUp(E element) {
    int at = getDirect().indexOf(element);
    if (at > 0) {
      getDirect().remove(at);
      getDirect().add(at - 1, element);
      setTouched(true);
      reassess();
    }
  }

  /** Moves {@code element} down in the list; noop if it's already last. */
  public void moveDown(E element) {
    int at = getDirect().indexOf(element);
    if (at > -1 && at < getDirect().size() - 1) {
      getDirect().remove(at);
      getDirect().add(at + 1, element);
      setTouched(true);
      reassess();
    }
  }

  /** @return a property that will reflect whether {@code element} is the last element. */
  public BooleanProperty isLast(final E element) {
    return booleanProperty(new DerivedValue<Boolean>(element + "IsLast") {
      public Boolean get() {
        List<E> l = ListProperty.this.get();
        return l != null && !l.isEmpty() && element.equals(l.get(l.size() - 1));
      }
    });
  }

  /** @return a property that will reflect the index of {@code element}. */
  public IntegerProperty indexOf(final E element) {
    return integerProperty(new DerivedValue<Integer>(element + "Index") {
      public Integer get() {
        List<E> l = ListProperty.this.get();
        return l == null ? -1 : l.indexOf(element);
      }
    });
  }

  /** @return a new {@link ListCursor} that can be used to move through the list. */
  public ListCursor<E> newCursor() {
    return new ListCursor<E>(this);
  }

  /**
   * Sorts our values by {@code comparator} (only once, to keep the sort
   * continually updated, see {@link #setComparator}).
   *
   * If we've already been sorted by comparator, it will reverse the order.
   */
  public void sort(Comparator<E> comparator) {
    if (lastComparator == comparator) {
      // Creating this new reverse comparator means we'll also "reset"
      // lastComparator to some other value, such that if we get called
      // with the same comparator again, we'll toggle back/forth the order
      //
      // Eventually we could get fancier and keep a stack of comparators.
      comparator = Collections.reverseOrder(comparator);
    }
    Collections.sort(getDirect(), comparator);
    lastComparator = comparator;
    reassess();
  }

  /**
   * Sorts our values by {@code comparator} (and continually applies
   * the comparator as new values are added/removed/set).
   */
  public void setComparator(Comparator<E> comparator) {
    this.persistentComparator = comparator;
    List<E> copy = copyLastValue(getDirect());
    if (copy != null && !copy.equals(getDirect())) {
      set(copy);
    }
  }

  /** Registers {@code handler} to be called when new values are added. */
  public HandlerRegistration addValueAddedHandler(final ValueAddedHandler<E> handler) {
    return addHandler(ValueAddedEvent.getType(), handler);
  }

  /** Registers {@code handler} to be called when values are removed. */
  public HandlerRegistration addValueRemovedHandler(final ValueRemovedHandler<E> handler) {
    return addHandler(ValueRemovedEvent.getType(), handler);
  }

  /** Registers {@code handler} to be called when the list changes. */
  public HandlerRegistration addListChangedHandler(final ListChangedHandler<E> handler) {
    return addHandler(ListChangedEvent.getType(), handler);
  }

  /** Registers {@code handler} to be called when values changed. */
  public HandlerRegistration addMemberChangedHandler(final MemberChangedHandler handler) {
    return addHandler(MemberChangedEvent.getType(), handler);
  }

  /**
   * Creates a new {@link ListProperty>} of type {@code F}.
   *
   * Any changes made to either list will be reflected in the other,
   * using {@code converter} to go between {@code E} and {@code F}.
   */
  public <F> ListProperty<F> as(final ElementConverter<E, F> converter) {
    // make an intial copy of all the elements currently in our list
    List<F> initial = null;
    if (get() != null) {
      initial = new ArrayList<F>();
      for (E e : get()) {
        F f = converter.to(e);
        initial.add(f);
      }
    }
    final ListProperty<F> as = listProperty(getName(), initial);
    final boolean[] active = { false };
    // keep converting E -> F into as
    addListChangedHandler(new ListChangedHandler<E>() {
      public void onListChanged(ListChangedEvent<E> event) {
        if (!active[0]) {
          active[0] = true;
          if (get() != null && as.get() == null) {
            as.setInitialValue(new ArrayList<F>());
          }
          event.getDiff().apply(as.getDirect(), new ListDiff.Mapper<E, F>() {
            public F map(E e) {
              return converter.to(e);
            }
          });
          if (isTouched() && !as.isTouched()) {
            as.setTouched(true);
          } else {
            as.reassess();
          }
          active[0] = false;
        }
      }
    });
    // also convert new Fs back into Es
    as.addListChangedHandler(new ListChangedHandler<F>() {
      public void onListChanged(ListChangedEvent<F> event) {
        if (!active[0]) {
          active[0] = true;
          if (get() == null && as.get() != null) {
            setInitialValue(new ArrayList<E>());
          }
          event.getDiff().apply(getDirect(), new ListDiff.Mapper<F, E>() {
            public E map(F f) {
              return converter.from(f);
            }
          });
          if (as.isTouched() && !isTouched()) {
            setTouched(true);
          } else {
            reassess();
          }
          active[0] = false;
        }
      }
    });
    return as;
  }

  /**
   * Creates a new {@link ListProperty>} of type {@code F}.
   *
   * Only changes in this list will be reflected in the returned
   * list, e.g. it's a one way conversion.
   */
  public <F> ListProperty<F> map(final ElementMapper<E, F> mapper) {
    // make an intial copy of all the elements currently in our list
    List<F> initial = null;
    if (get() != null) {
      initial = new ArrayList<F>();
      for (E e : get()) {
        initial.add(mapper.map(e));
      }
    }
    final ListProperty<F> mapped = listProperty(getName(), initial);
    // keep converting E -> F into as
    addListChangedHandler(new ListChangedHandler<E>() {
      public void onListChanged(ListChangedEvent<E> event) {
        if (get() != null && mapped.get() == null) {
          mapped.setInitialValue(new ArrayList<F>());
        }
        event.getDiff().apply(mapped.getDirect(), new ListDiff.Mapper<E, F>() {
          public F map(E e) {
            return mapper.map(e);
          }
        });
        if (isTouched() && !mapped.isTouched()) {
          mapped.setTouched(true);
        } else {
          mapped.reassess();
        }
      }
    });
    return mapped;
  }

  public ListProperty<E> filter(final ElementFilter<E> filter) {
    return listProperty(new DerivedValue<List<E>>(getValueObject().getName() + "Filtered") {
      public List<E> get() {
        List<E> filtered = new ArrayList<E>();
        if (ListProperty.this.get() != null) {
          for (E item : ListProperty.this.get()) {
            if (filter.matches(item)) {
              filtered.add(item);
            }
          }
        }
        return Collections.unmodifiableList(filtered);
      }
    });
  }

  /**
   * @return a property that, if we contain properties or models, will be true if all
   * contains properties/models (as well as ourself) are valid.
   */
  public Property<Boolean> allValid() {
    if (allValid == null) {
      allValid = new PropertyGroup(getValueObject().getName() + ".allValid");
      allValid.add(this);
      for (E element : getDirect()) {
        addToAllValidIfNeeded(element);
      }
    }
    return allValid;
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
    List<E> copy = new ArrayList<E>(newValue);
    if (persistentComparator != null) {
      Collections.sort(copy, persistentComparator);
    }
    return copy;
  }

  @Override
  protected void fireChanged(List<E> oldValue, List<E> newValue) {
    ListDiff<E> diff = ListDiff.of(oldValue, newValue);
    for (Location<E> added : diff.added) {
      fireEvent(new ValueAddedEvent<E>(added.element));
      listenForMemberChanged(added.element);
      addToAllValidIfNeeded(added.element);
    }
    for (Location<E> removed : diff.removed) {
      fireEvent(new ValueRemovedEvent<E>(removed.element));
      removeFromAllValidIfNeeded(removed.element);
    }
    fireEvent(new ListChangedEvent<E>(this, oldValue, newValue, diff));
    // if someone is listening for "did one of your models change", they
    // probably also care about a new model showing up/old model going away
    fireEvent(new MemberChangedEvent());
    super.fireChanged(oldValue, newValue);
  }

  private void addToAllValidIfNeeded(E element) {
    if (allValid != null) {
      if (element instanceof Property<?>) {
        allValid.add((Property<?>) element);
      } else if (element instanceof Model) {
        allValid.add(((Model) element).allValid());
      }
    }
  }

  private void removeFromAllValidIfNeeded(E element) {
    if (allValid != null) {
      if (element instanceof Property<?>) {
        allValid.remove((Property<?>) element);
      } else if (element instanceof Model) {
        allValid.remove(((Model) element).allValid());
      }
    }
  }

  private void sortIfNeeded() {
    if (persistentComparator != null) {
      Collections.sort(getDirect(), persistentComparator);
    }
  }

  private List<E> getDirect() {
    return super.get();
  }

  // Forwards member changed events on our models to our own model
  @SuppressWarnings("unchecked")
  private void listenForMemberChanged(final E item) {
    if (item instanceof HasMemberChangedHandlers) {
      ((HasMemberChangedHandlers) item).addMemberChangedHandler(new MemberChangedHandler() {
        public void onMemberChanged(MemberChangedEvent event) {
          // in case the item was removed, we don't currently unsubscribe
          if (getDirect().contains(item)) {
            fireEvent(event);
          }
        }
      });
    } else if (item instanceof Property<?>) {
      ((Property<Object>) item).addPropertyChangedHandler(new PropertyChangedHandler<Object>() {
        public void onPropertyChanged(PropertyChangedEvent<Object> event) {
          if (getDirect().contains(item)) {
            fireEvent(new MemberChangedEvent());
          }
        }
      });
    }
  }

  /**
   * Checks equality between a and b by ignoring list order.
   *
   * This is because a frequent use case of "listA.is(listB)" is for "Select All"
   * functionality, and if a user selects items in a different order, we still want
   * to consider listA equal to listB.
   */
  @Override
  protected boolean isEqual(List<E> a, List<E> b) {
    if (ObjectUtils.eq(a, b)) {
      return true;
    } else if (a != null && b != null && a.size() == b.size()) {
      List<E> b2 = new ArrayList<E>(b);
      for (E e : a) {
        if (!b2.remove(e)) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

}
