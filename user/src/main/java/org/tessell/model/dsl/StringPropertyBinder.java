package org.tessell.model.dsl;

import static org.tessell.util.StringUtils.sanitizeIfString;

import org.tessell.gwt.user.client.ui.IsTextBoxBase;
import org.tessell.model.properties.StringProperty;
import org.tessell.util.ObjectUtils;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.HasValue;

/** Binds StringProperties to widgets (special max length, etc. handling). */
public class StringPropertyBinder extends PropertyBinder<String> {

  private final StringProperty sp;

  public StringPropertyBinder(final Binder b, final StringProperty sp) {
    super(b, sp);
    this.sp = sp;
  }

  public <V extends HasValue<String> & HasKeyUpHandlers> void toKeyUp(final V source) {
    if (sp.getMaxLength() != null && source instanceof IsTextBoxBase) {
      ((IsTextBoxBase) source).setMaxLength(sp.getMaxLength());
    }
    b.add(sp.addPropertyChangedHandler(e -> {
      // Explicitly check if we need to source.setValue, because if this change
      // is due to a key up event, then source.getValue is already correct, but
      // calling the admittedly needless .setValue will reset the cursor to the
      // end of the input field
      if (!ObjectUtils.eq(e.getProperty().get(), source.getValue())) {
        source.setValue(e.getProperty().get(), true);
      }
    }));
    // set initial value
    source.setValue(sp.getValue(), true);
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      b.add(source.addKeyUpHandler(e -> {
        // We don't want to entirely sanitize (e.g. 'the ' => 'the'), but we
        // always want to do "" => null
        if ("".equals(source.getValue())) {
          p.set(null);
        } else {
          p.set(source.getValue());
        }
      }));
      b.add(source.addValueChangeHandler(e -> {
        p.set(sanitizeIfString(source.getValue()));
      }));
    }
  }
}
