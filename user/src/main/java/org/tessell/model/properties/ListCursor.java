package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.basicProperty;

import java.util.List;

/**
 * Represents some location within a {@link ListProperty}.
 *
 * This is basically just some helper methods for moving next/back in a
 * list, and asking if the current value is first/is last/etc.
 */
public class ListCursor<E> {

  private final ListProperty<E> list;
  private final BasicProperty<E> value = basicProperty("value");
  private Property<Boolean> isFirst;
  private Property<Boolean> isLast;

  public ListCursor(ListProperty<E> list) {
    this.list = list;
    list.nowAndOnChange(new PropertyValueHandler<List<E>>() {
      public void onValue(List<E> list) {
        if (list.isEmpty()) {
          value.setInitialValue(null);
        } else if (value.get() == null) {
          value.setInitialValue(list.get(0));
        }
      }
    });
  }

  /** Moves the cursor to the next list element, assuming we're not already last. */
  public void moveNext() {
    int i = currentIndex();
    if (i >= 0 && i < list.get().size() - 1) {
      value.set(list.get().get(i + 1));
    }
  }

  /** Moves the cursor to the previous list element, assuming we're not already first. */
  public void moveBack() {
    int i = currentIndex();
    if (i >= 1) {
      value.set(list.get().get(i - 1));
    }
  }

  /** @return the current value under the cursor. */
  public Property<E> value() {
    return value;
  }

  @SuppressWarnings("unchecked")
  public Property<Boolean> isFirst() {
    if (isFirst == null) {
      isFirst = NewProperty.and(list.first().is(value), value.isSet());
    }
    return isFirst;
  }

  @SuppressWarnings("unchecked")
  public Property<Boolean> isLast() {
    if (isLast == null) {
      isLast = NewProperty.and(list.last().is(value), value.isSet());
    }
    return isLast;
  }

  private int currentIndex() {
    return list.get() == null ? -1 : list.get().indexOf(value.get());
  }
}
