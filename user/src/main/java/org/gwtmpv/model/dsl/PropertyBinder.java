package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.toStr;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.IsTextList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

/** Binds properties to widgets. */
public class PropertyBinder<P> {

  private final Binder binder;
  private final Property<P> p;

  public PropertyBinder(Binder binder, Property<P> p) {
    this.binder = binder;
    this.p = p;
  }

  /** Binds our property to {@code element} (one-way). */
  public PropertyBinder<P> toTextOf(final HasText element) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        element.setText(toStr(p.get(), ""));
      }
    };
    h.onPropertyChanged(new PropertyChangedEvent<P>(p)); // set initial value
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  /** Binds our property to {@code source} (two-way). */
  public PropertyBinder<P> to(final HasValue<P> source) {
    binder.registerHandler(source.addValueChangeHandler(new ValueChangeHandler<P>() {
      public void onValueChange(ValueChangeEvent<P> event) {
        p.set(source.getValue());
      }
    }));
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setValue(event.getProperty().get(), true);
      }
    };
    h.onPropertyChanged(new PropertyChangedEvent<P>(p)); // set initial value
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  /** Binds errors for our property to {@code errors}. */
  public PropertyBinder<P> errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    binder.registerHandler(p.addRuleTriggeredHandler(i));
    binder.registerHandler(p.addRuleUntriggeredHandler(i));
    return this;
  }

  /** Binds our property to {@code source} and its errors to {@code errors}. */
  public PropertyBinder<P> to(final HasValue<P> source, IsTextList errors) {
    return to(source).errorsTo(errors);
  }

  /** @return a {@link ValueBinder} to our property for a specific value. */
  public ValueBinder<P> withValue(P value) {
    return new ValueBinder<P>(p, value);
  }

  /** Binds a specific value to a widget. */
  public class ValueBinder<P1> {
    private final Property<P1> p;
    private final P1 value;

    private ValueBinder(final Property<P1> p, final P1 value) {
      this.p = p;
      this.value = value;
    }

    public ValueBinder<P1> to(final HasClickHandlers clickable) {
      binder.registerHandler(clickable.addClickHandler(new BoundOnClick<P1>(p, value)));
      return this;
    }
  }

}
