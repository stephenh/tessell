package org.gwtmpv.model.util;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedEvent.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

public class Listen {

  /** Updates our property value when <code>hasValue</code> changes. */
  public static <P> HandlerRegistration listenTo(final HasValue<P> fromValue, final Property<P> toProperty) {
    return fromValue.addValueChangeHandler(new ValueChangeHandler<P>() {
      public void onValueChange(final ValueChangeEvent<P> event) {
        toProperty.set(event.getValue());
      }
    });
  }

  /** Fires a {@link PropertyChangingEvent} on key up for <code>hasKeyUp</code>. */
  public static <P, W extends HasAllKeyHandlers & HasValue<P>> HandlerRegistration listenIntently(final W fromWidget, final Property<P> toProperty) {
    return fromWidget.addKeyUpHandler(new KeyUpHandler() {
      public void onKeyUp(final KeyUpEvent event) {
        toProperty.set(fromWidget.getValue());
      }
    });
  }

  /** Updates our property when <code>hasValue</code> changes and updates <code>hasValue</code> when we change. */
  public static <P> HandlerRegistration[] bothWays(final HasValue<P> value, final Property<P> property) {
    // them --> us
    final HandlerRegistration a = value.addValueChangeHandler(new ValueChangeHandler<P>() {
      public void onValueChange(final ValueChangeEvent<P> event) {
        property.set(event.getValue()); // will fireChanged
      }
    });
    // us --> them
    final HandlerRegistration b = addAndFireIfSet(property, new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        value.setValue(event.getProperty().get());
      }
    });
    return new HandlerRegistration[] { a, b };
  }

  /** When our property changes, interpolates it into <code>format</code> and sets <code>hasText</code>. */
  public static <P> HandlerRegistration updateOnChanged(final Property<P> p, final HasText hasText, final String format) {
    return addAndFireIfSet(p, new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        final P value = event.getProperty().get();
        final String asString = value == null ? "" : value.toString();
        hasText.setText(format.replace("{}", asString));
      }
    });
  }

  private static <P> HandlerRegistration addAndFireIfSet(final Property<P> p, final PropertyChangedHandler<P> h) {
    h.onPropertyChanged(new PropertyChangedEvent<P>(p));
    return p.addPropertyChangedHandler(h);
  }

}
