package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.toStr;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.util.cookies.Cookie;
import org.gwtmpv.widgets.IsListBox;
import org.gwtmpv.widgets.IsTextList;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHTML;
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
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p));
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  /** Binds our property to {@code element} (one-way). */
  public PropertyBinder<P> toHtmlOf(final HasHTML element) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        element.setHTML(toStr(p.get(), ""));
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p));
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  public PropertyBinder<P> to(final Property<P> other) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        other.set(event.getProperty().get());
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p));
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  /** Binds our property to {@code source} (two-way). */
  public PropertyBinder<P> to(final HasValue<P> source) {
    PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setValue(event.getProperty().get(), true);
      }
    };
    // set initial value
    h.onPropertyChanged(new PropertyChangedEvent<P>(p));
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      binder.registerHandler(source.addValueChangeHandler(new ValueChangeHandler<P>() {
        public void onValueChange(ValueChangeEvent<P> event) {
          if ("".equals(source.getValue())) {
            p.set(null);
          } else {
            p.set(source.getValue());
          }
        }
      }));
    }
    binder.registerHandler(p.addPropertyChangedHandler(h));
    return this;
  }

  public HandlerRegistrations to(final IsListBox source, final ArrayList<P> options) {
    return to(source, options, new ListBoxIdentityAdaptor<P>());
  }

  public <O> HandlerRegistrations to(final IsListBox source, final ArrayList<O> options, final ListBoxAdaptor<P, O> adaptor) {
    int i = 0;
    for (O option : options) {
      source.addItem(adaptor.toDisplay(option), Integer.toString(i++));
    }
    if (p.get() == null) {
      // TODO don't currently support an empty option
      p.set(adaptor.toValue(options.get(0)));
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
          if (adaptor.toValue(option).equals(p.get())) {
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
  public PropertyBinder<P> errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    binder.registerHandler(p.addRuleTriggeredHandler(i));
    binder.registerHandler(p.addRuleUntriggeredHandler(i));
    i.addExisting(p);
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
      binder.registerHandler(clickable.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent e) {
          p.set(value);
        }
      }));
      return this;
    }
  }

}
