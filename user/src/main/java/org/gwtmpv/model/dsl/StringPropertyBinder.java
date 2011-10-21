package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.widgets.IsTextBox;

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
