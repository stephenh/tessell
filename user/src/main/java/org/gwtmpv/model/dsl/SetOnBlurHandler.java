package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HasValue;

public class SetOnBlurHandler<T> implements BlurHandler {

  private final Property<T> property;
  private final HasValue<T> target;

  public SetOnBlurHandler(final Property<T> property, final HasValue<T> target) {
    this.property = property;
    this.target = target;
  }

  @Override
  public void onBlur(final BlurEvent event) {
    property.set(target.getValue());
  }

}
