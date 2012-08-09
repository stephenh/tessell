package org.tessell.model.dsl;

import java.util.List;

import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;
import org.tessell.util.ObjectUtils;
import org.tessell.widgets.IsTextList;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;

/** Binds properties to widgets. */
public class PropertyBinder<P> {

  protected final Property<P> p;

  public PropertyBinder(final Property<P> p) {
    this.p = p;
  }

  /** Binds our property to {@code value} (one-way). */
  public HandlerRegistrations to(final TakesValue<P> value) {
    final PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        value.setValue(p.get());
      }
    };
    // Set initial value. Even though this is one-way, if value is a cookie/etc.,
    // we may want to set the initial value of our property back to the current
    // value of value. Do this only one, and only if the property looks unset
    // (non-touched and null).
    if (!p.isReadOnly() && !p.isTouched() && p.get() == null && value.getValue() != null) {
      p.set(value.getValue());
    } else {
      value.setValue(p.get());
    }
    return new HandlerRegistrations(p.addPropertyChangedHandler(h));
  }

  /** Binds our property to {@code source} (two-way). */
  public HandlerRegistrations to(final HasValue<P> source) {
    final HandlerRegistrations hr = new HandlerRegistrations();
    // set initial value
    source.setValue(p.get(), true);
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      hr.add(source.addValueChangeHandler(new ValueChangeHandler<P>() {
        public void onValueChange(final ValueChangeEvent<P> event) {
          p.set(sanitizeIfString(source.getValue()));
        }
      }));
    }
    hr.add(p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setValue(event.getProperty().get(), true);
      }
    }));
    return hr;
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public HandlerRegistrations to(final IsListBox source, final List<P> options) {
    return to(source, options, new ListBoxIdentityAdaptor<P>());
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public <O> HandlerRegistrations to(final IsListBox source, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    int i = 0;
    for (final O option : options) {
      source.addItem(adaptor.toDisplay(option), Integer.toString(i++));
    }
    if (p.get() == null) {
      if (!options.contains(null)) {
        p.set(adaptor.toValue(options.get(0)));
      }
    }
    source.setSelectedIndex(options.indexOf(p.get()));
    final HandlerRegistration a = source.addChangeHandler(new ChangeHandler() {
      public void onChange(final ChangeEvent event) {
        final int i = source.getSelectedIndex();
        if (i == -1) {
          p.set(null);
        } else {
          p.set(adaptor.toValue(options.get(i)));
        }
      }
    });
    final HandlerRegistration b = p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setSelectedIndex(indexInOptions());
      }

      // can't use indexOf because we can't map value -> option, only option -> value
      private int indexInOptions() {
        int i = 0;
        for (final O option : options) {
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

  /** Binds errors for our property to {@code errors}. */
  public HandlerRegistrations errorsTo(final IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    final HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(p.addRuleTriggeredHandler(i));
    hr.add(p.addRuleUntriggeredHandler(i));
    i.addExisting(p);
    return hr;
  }

  /** Binds our property to {@code source} and its errors to {@code errors}. */
  public HandlerRegistrations to(final HasValue<P> source, final IsTextList errors) {
    final HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(to(source));
    hr.add(errorsTo(errors));
    return hr;
  }

  /** @return a {@link ValueBinder} to our property for a specific value. */
  public ValueBinder<P> withValue(final P value) {
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
        public void onClick(final ClickEvent e) {
          p.set(value);
        }
      }));
    }
  }

  @SuppressWarnings("unchecked")
  protected static <P> P sanitizeIfString(P value) {
    if (value instanceof String) {
      value = (P) ((String) value).trim();
      if ("".equals(value)) {
        value = null;
      }
    }
    return value;
  }

}
