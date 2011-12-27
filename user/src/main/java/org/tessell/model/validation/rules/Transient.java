package org.tessell.model.validation.rules;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;
import org.tessell.model.validation.Valid;

/** A rule that fires immediately, until the source property changes. */
public class Transient<T> extends AbstractRule<T, Transient<T>> {

  private boolean hasChanged;

  public Transient(final Property<T> property, String message) {
    super(property, message);
    property.reassess();
    property.addPropertyChangedHandler(new PropertyChangedHandler<T>() {
      public void onPropertyChanged(PropertyChangedEvent<T> event) {
        hasChanged = true;
        property.reassess();
      }
    });
  }

  @Override
  protected Valid isValid() {
    return hasChanged ? Valid.YES : Valid.NO;
  }

  @Override
  protected Transient<T> getThis() {
    return this;
  }

}
