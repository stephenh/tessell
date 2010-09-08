package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasValue;

public class SetOnKeyUpHandler<T> implements KeyUpHandler {

  private final Property<T> property;
  private final HasValue<T> target;

  public SetOnKeyUpHandler(final Property<T> property, final HasValue<T> target) {
    this.property = property;
    this.target = target;
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {
    property.set(target.getValue());
  }

}
