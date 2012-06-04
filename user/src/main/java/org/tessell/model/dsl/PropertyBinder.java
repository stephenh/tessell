package org.tessell.model.dsl;

import java.util.List;

import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;
import org.tessell.util.ObjectUtils;
import org.tessell.util.cookies.Cookie;
import org.tessell.widgets.IsTextList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;

/** Binds properties to widgets. */
public class PropertyBinder<P> {

  protected final Property<P> p;

  public PropertyBinder(Property<P> p) {
    this.p = p;
  }

  /** Binds our property to {@code value} (one-way). */
  public HandlerRegistrations to(final TakesValue<P> value) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        value.setValue(p.get());
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p, null, value.getValue()));
    return new HandlerRegistrations(p.addPropertyChangedHandler(h));
  }

  /** Sets up one-way binding from this property to {@code other}. */
  public HandlerRegistrations to(final Property<P> other) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        other.set(event.getProperty().get());
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p, null, other.get()));
    return new HandlerRegistrations(p.addPropertyChangedHandler(h));
  }

  /** Binds our property to {@code source} (two-way). */
  public HandlerRegistrations to(final HasValue<P> source) {
    HandlerRegistrations hr = new HandlerRegistrations();
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setValue(event.getProperty().get(), true);
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p, null, source.getValue()));
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      hr.add(source.addValueChangeHandler(new ValueChangeHandler<P>() {
        @SuppressWarnings("unchecked")
        public void onValueChange(ValueChangeEvent<P> event) {
          P value = source.getValue();
          if (value instanceof String) {
            value = (P) ((String) value).trim();
            if (value.equals("")) {
              value = null;
            }
          }
          p.set(value);
        }
      }));
    }
    hr.add(p.addPropertyChangedHandler(h));
    return hr;
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public HandlerRegistrations to(final IsListBox source, final List<P> options) {
    return to(source, options, new ListBoxIdentityAdaptor<P>());
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public <O> HandlerRegistrations to(final IsListBox source, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    int i = 0;
    for (O option : options) {
      source.addItem(adaptor.toDisplay(option), Integer.toString(i++));
    }
    if (p.get() == null) {
      if (!options.contains(null)) {
        p.set(adaptor.toValue(options.get(0)));
      }
    }
    source.setSelectedIndex(options.indexOf(p.get()));
    HandlerRegistration a = source.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        int i = source.getSelectedIndex();
        if (i == -1) {
          p.set(null);
        } else {
          p.set(adaptor.toValue(options.get(i)));
        }
      }
    });
    HandlerRegistration b = p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        source.setSelectedIndex(indexInOptions());
      }

      // can't use indexOf because we can't map value -> option, only option -> value
      private int indexInOptions() {
        int i = 0;
        for (O option : options) {
          if (ObjectUtils.eq(adaptor.toValue(option), p.get())) {
            return i;
          }
          i++;
        }
        return -1;
      }
    });
    return new HandlerRegistrations(a, b);
  }

  /** Binds changes of {@code p} to {@code cookie} (not two-way as cookies don't fire change events). */
  public HandlerRegistrations to(final Cookie<P> cookie) {
    HandlerRegistration a = p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        cookie.set(p.get());
      }
    });
    // set the initial value
    if (cookie.get() != null) {
      p.set(cookie.get());
    }
    return new HandlerRegistrations(a);
  }

  /** Binds errors for our property to {@code errors}. */
  public HandlerRegistrations errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(p.addRuleTriggeredHandler(i));
    hr.add(p.addRuleUntriggeredHandler(i));
    i.addExisting(p);
    return hr;
  }

  /** Binds our property to {@code source} and its errors to {@code errors}. */
  public HandlerRegistrations to(final HasValue<P> source, IsTextList errors) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(to(source));
    hr.add(errorsTo(errors));
    return hr;
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

    public HandlerRegistrations to(final HasClickHandlers clickable) {
      return new HandlerRegistrations(clickable.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent e) {
          p.set(value);
        }
      }));
    }
  }

}
