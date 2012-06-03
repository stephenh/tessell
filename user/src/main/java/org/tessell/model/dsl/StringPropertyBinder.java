package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.IsTextBox;
import org.tessell.model.properties.StringProperty;

import com.google.gwt.user.client.ui.HasValue;

/** Binds StringProperties to widgets (special max length, etc. handling). */
public class StringPropertyBinder extends PropertyBinder<String> {

  private final StringProperty sp;

  public StringPropertyBinder(StringProperty sp) {
    super(sp);
    this.sp = sp;
  }

  @Override
  public HandlerRegistrations to(final HasValue<String> source) {
    if (sp.getMaxLength() != null && source instanceof IsTextBox) {
      ((IsTextBox) source).setMaxLength(sp.getMaxLength());
    }
    return super.to(source);
  }
}
