package org.gwtmpv.model.validation.rules;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.Valid;

/** A rule that fires immediately, until the source property changes. */
public class Transient<T> extends AbstractRule<T, Transient<T>> {

  private boolean hasChanged;

  public Transient(Property<T> property, String message) {
    super(property, message);
    property.reassess();
    property.addPropertyChangedHandler(new PropertyChangedHandler<T>() {
      public void onPropertyChanged(PropertyChangedEvent<T> event) {
        hasChanged = true;
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
