package org.tessell.model.validation.rules;

import java.util.List;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;

/** Validates that a property is not-null. */
public class Required extends AbstractRule<Object> {

  public Required(final String message) {
    super(message);
  }

  public Required() {
    super(null); // will get by setProperty
  }

  @Override
  public void setProperty(Property<Object> property) {
    super.setProperty(property);
    if (message == null) {
      message = property.getName() + " is required";
    }
  }

  @Override
  protected Valid isValid() {
    final Object value = property.get();
    if (value instanceof String) {
      // hack to treat empty strings as not entered
      return (value != null && ((String) value).length() > 0) ? Valid.YES : Valid.NO;
    } else if (value instanceof List) {
      // hack to require lists to be non-empty
      return (value != null && ((List<?>) value).size() > 0) ? Valid.YES : Valid.NO;
    } else {
      return value != null ? Valid.YES : Valid.NO;
    }
  }

}
