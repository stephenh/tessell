package org.tessell.tests.model.properties;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

public class CountChanges {
  public static <T> CountChanges on(Property<T> source) {
    final CountChanges c = new CountChanges();
    source.addPropertyChangedHandler(new PropertyChangedHandler<T>() {
      public void onPropertyChanged(PropertyChangedEvent<T> event) {
        c.changes++;
      }
    });
    return c;
  }

  public int changes;
}