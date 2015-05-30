package org.tessell.model.dsl;

import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.ListProperty;

public class BooleanPropertyToListPropertyBinder<V> {

  private final Binder b;
  private final BooleanProperty p;
  private final ListProperty<V> values;

  public BooleanPropertyToListPropertyBinder(Binder b, BooleanProperty p, ListProperty<V> values) {
    this.b = b;
    this.p = p;
    this.values = values;
  }

  public void has(final V value) {
    b.add(p.addPropertyChangedHandler(e -> update(value)));
    b.add(values.addPropertyChangedHandler(e -> {
      p.set(e.getNewValue().contains(value));
    }));
    if (b.canSetInitialValue(p)) {
      p.setInitialValue(values.get().contains(value));
    } else {
      update(value); // set initial
    }
  }

  private void update(V value) {
    if (p.isTrue() && !values.get().contains(value)) {
      values.add(value);
    } else if (!p.isTrue() && values.get().contains(value)) {
      values.remove(value);
    }
  }
}
