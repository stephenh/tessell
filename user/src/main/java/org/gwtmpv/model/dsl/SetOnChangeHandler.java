package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

public class SetOnChangeHandler<T> implements ValueChangeHandler<T> {

  private final Property<T> property;
  private final HasValue<T> target;

  public SetOnChangeHandler(final Property<T> property, final HasValue<T> target) {
    this.property = property;
    this.target = target;
  }

  @Override
  public void onValueChange(ValueChangeEvent<T> event) {
    property.set(target.getValue());
  }

}
