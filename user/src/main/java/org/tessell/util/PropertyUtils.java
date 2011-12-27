package org.tessell.util;

import org.tessell.model.properties.BasicProperty;
import org.tessell.model.properties.Property;
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

}
