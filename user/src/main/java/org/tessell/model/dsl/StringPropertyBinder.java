package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.IsTextBox;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.StringProperty;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

/** Binds StringProperties to widgets (special max length, etc. handling). */
public class StringPropertyBinder extends PropertyBinder<String> {

  private final StringProperty sp;

  public StringPropertyBinder(final StringProperty sp) {
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

  public <V extends HasValue<String> & HasKeyUpHandlers> HandlerRegistrations toKeyUp(final V source) {
    if (sp.getMaxLength() != null && source instanceof IsTextBox) {
      ((IsTextBox) source).setMaxLength(sp.getMaxLength());
    }
    final HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(sp.addPropertyChangedHandler(new PropertyChangedHandler<String>() {
      public void onPropertyChanged(final PropertyChangedEvent<String> event) {
        source.setValue(event.getProperty().get(), true);
      }
    }));
    // set initial value
    source.setValue(sp.getValue(), true);
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      hr.add(source.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(final KeyUpEvent event) {
          p.set(source.getValue());
        }
      }));
      hr.add(source.addValueChangeHandler(new ValueChangeHandler<String>() {
        public void onValueChange(final ValueChangeEvent<String> event) {
          p.set(sanitizeIfString(source.getValue()));
        }
      }));
    }
    return hr;
  }
}
