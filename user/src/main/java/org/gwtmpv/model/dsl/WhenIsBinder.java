package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.widgets.HasCss;

import com.google.gwt.dom.client.Style.Display;

public class WhenIsBinder<P> {

  private final Binder binder;
  private final Property<P> property;
  private final P value;
  private boolean trigged = false;

  public WhenIsBinder(Binder binder, Property<P> property, P value) {
    this.binder = binder;
    this.property = property;
    this.value = value;
  }

  public WhenIsSetBinder<P> set(String style) {
    return new WhenIsSetBinder<P>(binder, property, value, style);
  }

  public <V> WhenIsRemoveBinder<P, V> remove(V newValue) {
    return new WhenIsRemoveBinder<P, V>(binder, property, value, newValue);
  }

  public void show(final HasCss css) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        showIfEq(css);
      }
    }));
    showIfEq(css); // set initial
  }

  public void error(final String message) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        errorIfEq(message);
      }
    }));
    errorIfEq(message);
  }

  private void errorIfEq(String message) {
    if (eq(value, property.get())) {
      property.fireEvent(new RuleTriggeredEvent(this, message, new Boolean[] { false }));
      trigged = true;
    } else if (trigged) {
      property.fireEvent(new RuleUntriggeredEvent(this, message));
      trigged = false;
    }
  }

  private void showIfEq(HasCss css) {
    if (eq(value, property.get())) {
      css.getStyle().clearDisplay();
    } else {
      css.getStyle().setDisplay(Display.NONE);
    }
  }
}
