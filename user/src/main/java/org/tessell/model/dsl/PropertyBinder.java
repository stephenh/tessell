package org.tessell.model.dsl;

import java.util.List;

import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;
import org.tessell.util.ObjectUtils;
import org.tessell.widgets.IsTextList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;

/** Binds properties to widgets. */
public class PropertyBinder<P> {

  protected final Binder b;
  protected final Property<P> p;

  public PropertyBinder(final Binder b, final Property<P> p) {
    this.b = b;
    this.p = p;
  }

  /** Binds our property to {@code value} (one-way). */
  public void to(final TakesValue<P> value) {
    b.add(p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        value.setValue(p.get());
      }
    }));
    // Set initial value. Even though this is one-way, if value is a cookie/etc.,
    // we may want to set the initial value of our property back to the current
    // value of value. Do this only one, and only if the property looks unset
    // (non-touched and null).
    if (b.canSetInitialValue(p) && value.getValue() != null) {
      p.setInitialValue(value.getValue());
    } else {
      value.setValue(p.get());
    }
  }

  /** Binds our property to {@code source} (two-way). */
  public void to(final HasValue<P> source) {
    // set initial value
    source.setValue(p.get(), true);
    // after we've set the initial value (which fired ValueChangeEvent and
    // would have messed up our 'touched' state), listen for others changes
    if (!p.isReadOnly()) {
      b.add(source.addValueChangeHandler(new ValueChangeHandler<P>() {
        public void onValueChange(final ValueChangeEvent<P> event) {
          p.set(sanitizeIfString(source.getValue()));
        }
      }));
    }
    b.add(p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(final PropertyChangedEvent<P> event) {
        source.setValue(event.getProperty().get(), true);
      }
    }));
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public void to(final IsListBox source, final List<P> options) {
    to(source, options, new ListBoxIdentityAdaptor<P>());
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public <O> void to(final IsListBox source, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
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
    b.add(source.addChangeHandler(new ChangeHandler() {
      public void onChange(final ChangeEvent event) {
        final int i = source.getSelectedIndex();
        if (i == -1) {
          p.set(null);
        } else {
          p.set(adaptor.toValue(options.get(i)));
        }
      }
    }));
    b.add(p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
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
    }));
  }

  /** Binds errors for our property to {@code errors}. */
  public void errorsTo(final IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    b.add(p.addRuleTriggeredHandler(i));
    b.add(p.addRuleUntriggeredHandler(i));
    i.addExisting(p);
  }

  /** Binds our property to {@code source} and its errors to {@code errors}. */
  public void to(final HasValue<P> source, final IsTextList errors) {
    to(source);
    errorsTo(errors);
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
