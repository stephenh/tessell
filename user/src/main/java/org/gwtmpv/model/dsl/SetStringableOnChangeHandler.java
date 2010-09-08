package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.StringableProperty;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

public class SetStringableOnChangeHandler implements ValueChangeHandler<String> {

  private final StringableProperty property;
  private final HasValue<String> target;

  public SetStringableOnChangeHandler(final StringableProperty property, final HasValue<String> target) {
    this.property = property;
    this.target = target;
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    property.setAsString(target.getValue());
  }

}
