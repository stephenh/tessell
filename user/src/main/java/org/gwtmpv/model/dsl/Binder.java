package org.gwtmpv.model.dsl;

import org.gwtmpv.bus.CanRegisterHandlers;
import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.widgets.IsTextBox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
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

  public <P> WhenBinder<P> when(Property<P> property) {
    return new WhenBinder<P>(this, property);
  }

  /** Enhances each {@code source} to fire change events on key up and blur. */
  public <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> Binder enhance(S... sources) {
    for (final S source : sources) {
      registerHandler(source.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(final KeyUpEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
      registerHandler(source.addBlurHandler(new BlurHandler() {
        public void onBlur(final BlurEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
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

}
