package org.gwtmpv.model.dsl;

import org.gwtmpv.bus.CanRegisterHandlers;
import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.properties.StringableProperty;
import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
    return new PropertyBinder<P>(this, property);
  }

  public RuleBinder bind(Rule rule) {
    return new RuleBinder(this, rule);
  }

  /** @return a fluent {@link StringPropertyBinder} against {@code property}. */
  public <P> StringPropertyBinder bind(StringProperty property) {
    return new StringPropertyBinder(property);
  }

  /** @return a fluent {@link UiCommandBinder} against {@code command}. */
  public UiCommandBinder bind(UiCommand command) {
    return new UiCommandBinder(this, command);
  }

  public BooleanBinder whileTrue(Property<Boolean> property) {
    return new BooleanBinder(this, property);
  }

  /** @return a fluent {@link FormattedPropertyBinder} against {@code property}. */
  // this method sucks as you can't substitute the formatter
  // StringableProperty in general just needs to go away
  public <P, U extends StringableProperty & Property<P>> FormattedPropertyBinder<P, U> bindFormatted(U property) {
    return new FormattedPropertyBinder<P, U>(property);
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

  void registerHandler(HandlerRegistration r) {
    if (handlersOwner != null) {
      handlersOwner.registerHandler(r);
    }
  }

  /** Binds StringProperties to widgets (special max length, etc. handling). */
  public class StringPropertyBinder extends PropertyBinder<String> {
    private final StringProperty sp;

    private StringPropertyBinder(StringProperty sp) {
      super(Binder.this, sp);
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

  /** Methods to bind a StringableProperty to HasValue<String> widgets--needs StringableProperty to go away. */
  public class FormattedPropertyBinder<P, U extends StringableProperty & Property<P>> {
    private final U p;

    private FormattedPropertyBinder(U p) {
      this.p = p;
    }

    /** Binds our property to {@code source} and its errors to {@code errors}. */
    public <S extends HasBlurHandlers & HasValue<String>> FormattedPropertyBinder<P, U> to(final S source, IsTextList errors) {
      return to(source).errorsTo(errors);
    }

    /** Binds our property to {@code source}. */
    public FormattedPropertyBinder<P, U> to(final HasValue<String> source) {
      registerHandler(source.addValueChangeHandler(new ValueChangeHandler<String>() {
        public void onValueChange(ValueChangeEvent<String> event) {
          p.setAsString(source.getValue());
        }
      }));
      PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
        public void onPropertyChanged(final PropertyChangedEvent<P> event) {
          source.setValue(p.getAsString());
        }
      };
      h.onPropertyChanged(new PropertyChangedEvent<P>(p));
      registerHandler(p.addPropertyChangedHandler(h));
      return this;
    }

    /** Binds errors for our property to {@code errors}. */
    public FormattedPropertyBinder<P, U> errorsTo(IsTextList errors) {
      final TextListOnError i = new TextListOnError(errors);
      registerHandler(p.addRuleTriggeredHandler(i));
      registerHandler(p.addRuleUntriggeredHandler(i));
      return this;
    }
  }

}
