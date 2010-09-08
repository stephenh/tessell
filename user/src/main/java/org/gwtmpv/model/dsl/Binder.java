package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.toStr;

import org.gwtmpv.bus.CanRegisterHandlers;
import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedEvent.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.properties.StringableProperty;
import org.gwtmpv.widgets.IsElement;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Provides a fluent interface for binding properties to widgets.
 * 
 * Very heavily influenced by gwt-pectin.
 */
public class Binder {

  private final CanRegisterHandlers handlersOwner;

  public Binder(CanRegisterHandlers handlersOwner) {
    this.handlersOwner = handlersOwner;
  }

  /** @return a fluent {@link PropertyBinder} against {@code property}. */
  public <P> PropertyBinder<P> bind(Property<P> property) {
    return new PropertyBinder<P>(property);
  }

  /** @return a fluent {@link StringPropertyBinder} against {@code property}. */
  public <P> StringPropertyBinder bind(StringProperty property) {
    return new StringPropertyBinder(property);
  }

  /** @return a fluent {@link StringPropertyBinder} against {@code property}. */
  public <P> StringPropertyBinder bind(StringableProperty property) {
    return new StringPropertyBinder(property);
  }

  /** Enhances each {@code source} to fire change events on key up and blur. */
  public <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> Binder enhance(S... sources) {
    for (final S source : sources) {
      source.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(final KeyUpEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      });
      source.addBlurHandler(new BlurHandler() {
        public void onBlur(final BlurEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      });
    }
    return this;
  }

  private void registerHandler(HandlerRegistration r) {
    handlersOwner.registerHandler(r);
  }

  /** Binds properties to widgets. */
  public class PropertyBinder<P> {
    protected final Property<P> p;

    private PropertyBinder(Property<P> p) {
      this.p = p;
    }

    /** Binds our property to {@code element} (one-way). */
    public PropertyBinder<P> toTextOf(final IsElement element) {
      PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
        public void onPropertyChanged(final PropertyChangedEvent<P> event) {
          element.setInnerText(toStr(p.get(), ""));
        }
      };
      h.onPropertyChanged(new PropertyChangedEvent<P>(p));
      registerHandler(p.addPropertyChangedHandler(h));
      return this;
    }

    /** Binds our property to {@code source}. */
    public PropertyBinder<P> to(final HasValue<P> source) {
      registerHandler(source.addValueChangeHandler(new SetOnChangeHandler<P>(p, source)));
      PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
        public void onPropertyChanged(final PropertyChangedEvent<P> event) {
          source.setValue(event.getProperty().get());
        }
      };
      h.onPropertyChanged(new PropertyChangedEvent<P>(p));
      registerHandler(p.addPropertyChangedHandler(h));
      return this;
    }

    /** Binds errors for our property to {@code errors}. */
    public PropertyBinder<P> errorsTo(IsTextList errors) {
      final TextListOnError i = new TextListOnError(errors);
      registerHandler(p.addRuleTriggeredHandler(i));
      registerHandler(p.addRuleUntriggeredHandler(i));
      return this;
    }

    /** Binds our property to {@code source} and its errors to {@code errors}. */
    public <S extends HasBlurHandlers & HasValue<P>> PropertyBinder<P> to(final S source, IsTextList errors) {
      return to(source).errorsTo(errors);
    }

    /** @return a {@link ValueBinder} to our property for a specific value. */
    public ValueBinder<P> withValue(P value) {
      return new ValueBinder<P>(p, value);
    }
  }

  /** Binds StringProperties to widgets (special max length, etc. handling). */
  public class StringPropertyBinder extends PropertyBinder<String> {
    private final StringProperty sp;

    private StringPropertyBinder(StringProperty sp) {
      super(sp);
      this.sp = sp;
    }

    @Override
    public StringPropertyBinder to(final HasValue<String> source) {
      if (sp.getMaxLength() != null && source instanceof IsTextBox) {
        ((IsTextBox) source).setMaxLength(sp.getMaxLength());
      }
      super.to(source);
      return this;
    }
  }

  /** Binds a specific value to a widget. */
  public class ValueBinder<P> {
    private final Property<P> p;
    private final P value;

    private ValueBinder(Property<P> p, P value) {
      this.p = p;
      this.value = value;
    }

    public ValueBinder<P> to(HasClickHandlers clickable) {
      registerHandler(clickable.addClickHandler(new BoundOnClick<P>(p, value)));
      return this;
    }
  }

}
