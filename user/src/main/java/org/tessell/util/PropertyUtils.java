package org.tessell.util;

import org.tessell.model.Model;
import org.tessell.model.events.ValueAddedEvent;
import org.tessell.model.events.ValueAddedHandler;
import org.tessell.model.events.ValueRemovedEvent;
import org.tessell.model.events.ValueRemovedHandler;
import org.tessell.model.properties.BasicProperty;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.model.values.DerivedValue;

public class PropertyUtils {

  /** @return a property that will use {@code defaultValue} if {@code original} is {@code null} */
  public static <T> Property<T> defaultValue(final Property<T> original, final T defaultValue) {
    return original.addDerived(new BasicProperty<T>(new DerivedValue<T>() {
      public T get() {
        return ObjectUtils.defaultValue(original.get(), defaultValue);
      }
    }));
  }

  /** Adds/removes the model's {@code allValid} property in {@code models} to/from {@code all}. */
  public static <M extends Model> void syncModelsToGroup(final PropertyGroup all, final ListProperty<M> models) {
    models.addValueAddedHandler(new ValueAddedHandler<M>() {
      public void onValueAdded(ValueAddedEvent<M> event) {
        all.add(event.getValue().allValid());
      }
    });
    models.addValueRemovedHandler(new ValueRemovedHandler<M>() {
      public void onValueRemoved(ValueRemovedEvent<M> event) {
        all.remove(event.getValue().allValid());
      }
    });
    if (models.get() != null) {
      for (M model : models.get()) {
        all.add(model.allValid());
      }
    }
  }

}
