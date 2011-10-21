package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.widgets.HasCss;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.HasEnabled;

public class WhenIsBinder<P> {

  private final Property<P> property;
  private final P value;
  private boolean trigged = false;

  public WhenIsBinder(Property<P> property, P value) {
    this.property = property;
    this.value = value;
  }

  public WhenIsSetBinder<P> set(String style) {
    return new WhenIsSetBinder<P>(property, value, style);
  }

  public <V> WhenIsRemoveBinder<P, V> remove(V newValue) {
    return new WhenIsRemoveBinder<P, V>(property, value, newValue);
  }

  public <V> WhenIsAddBinder<P, V> add(V newValue) {
    return new WhenIsAddBinder<P, V>(property, value, newValue);
  }

  public HandlerRegistrations show(final HasCss... csses) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        showIfEq(csses);
      }
    }));
    showIfEq(csses); // set initial
    return hr;
  }

  public HandlerRegistrations hide(final HasCss... csses) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        hideIfEq(csses);
      }
    }));
    hideIfEq(csses); // set initial
    return hr;
  }

  public HandlerRegistrations visible(final HasCss css) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        visibleIfEq(css);
      }
    }));
    visibleIfEq(css); // set initial
    return hr;
  }

  public HandlerRegistrations error(final String message) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        errorIfEq(message);
      }
    }));
    errorIfEq(message);
    return hr;
  }

  public HandlerRegistrations enable(final HasEnabled enabled) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(enabled);
      }
    }));
    update(enabled); // set initial value
    return hr;
  }

  private void update(HasEnabled enabled) {
    if (eq(value, property.get())) {
      enabled.setEnabled(true);
    } else {
      enabled.setEnabled(false);
    }
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

  private void showIfEq(HasCss... csses) {
    if (eq(value, property.get())) {
      for (HasCss css : csses) {
        css.getStyle().clearDisplay();
      }
    } else {
      for (HasCss css : csses) {
        css.getStyle().setDisplay(Display.NONE);
      }
    }
  }

  private void hideIfEq(HasCss... csses) {
    if (eq(value, property.get())) {
      for (HasCss css : csses) {
        css.getStyle().setDisplay(Display.NONE);
      }
    } else {
      for (HasCss css : csses) {
        css.getStyle().clearDisplay();
      }
    }
  }

  private void visibleIfEq(HasCss css) {
    if (eq(value, property.get())) {
      css.getStyle().clearVisibility();
    } else {
      css.getStyle().setVisibility(Visibility.HIDDEN);
    }
  }
}
