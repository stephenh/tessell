package org.gwtmpv.util;

import org.gwtmpv.model.properties.BasicProperty;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.values.DerivedValue;

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
