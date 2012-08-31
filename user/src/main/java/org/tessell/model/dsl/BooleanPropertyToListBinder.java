package org.tessell.model.dsl;

import java.util.List;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.BooleanProperty;

public class BooleanPropertyToListBinder<V> {

  private final Binder b;
  private final BooleanProperty p;
  private final List<V> values;

  public BooleanPropertyToListBinder(Binder b, BooleanProperty p, List<V> values) {
    this.b = b;
    this.p = p;
    this.values = values;
  }

  public void has(final V value) {
    b.add(p.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        update(value);
      }
    }));
    if (b.canSetInitialValue(p)) {
      p.setInitial(values.contains(value));
    } else {
      update(value); // set initial
    }
  }

  private void update(V value) {
    if (p.isTrue() && !values.contains(value)) {
      values.add(value);
    } else if (!p.isTrue() && values.contains(value)) {
      values.remove(value);
    }
  }
}
