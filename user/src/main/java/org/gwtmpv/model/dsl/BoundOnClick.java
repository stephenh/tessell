package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class BoundOnClick<T> implements ClickHandler {

  private final Property<T> property;
  private final T value;

  public BoundOnClick(final Property<T> property, final T value) {
    this.property = property;
    this.value = value;
  }

  @Override
  public void onClick(final ClickEvent event) {
    property.set(value);
  }

}
