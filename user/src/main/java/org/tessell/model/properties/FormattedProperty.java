package org.tessell.model.properties;

import java.util.Map;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.validation.rules.Static;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Converts a source property to/from another type, using a {@link PropertyFormatter}.
 *
 * @param <SP> the source property type
 * @param <DP> the destination property type
 */
public class FormattedProperty<DP, SP> extends AbstractAbstractProperty<DP> {

  private final Property<SP> source;
  private final PropertyFormatter<SP, DP> formatter;
  private final Static isValid;

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter) {
    this(source, formatter, source.getName() + " is invalid");
  }

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter, final String message) {
    this.source = source;
    this.formatter = formatter;
    // note, we currently fire the error against our source property, so that people listening for errors
    // to it will see them. it might make more sense to fire against us first, then our source property.
    isValid = new Static(message) {
      @Override
      public boolean isImportant() {
        return true;
      }
    };
    source.addRule(isValid);
    source.addPropertyChangedHandler(new PropertyChangedHandler<SP>() {
      public void onPropertyChanged(PropertyChangedEvent<SP> event) {
        isValid.set(true); // any real (non-formatted) value being set makes our old attempt worthless
      }
    });
  }

  @Override
  public String toString() {
    return source.getValueName() + " " + get();
  }

  @Override
  public DP get() {
    SP value = source.get();
    return (value == null) ? formatter.nullValue() : formatter.format(value);
  }

  @Override
  public void set(DP value) {
    set(value, true);
  }

  @Override
  public void set(DP value, boolean shouldTouch) {
    // null and "" are special
    if (value == null || "".equals(value)) {
      isValid.set(true);
      source.set(null, shouldTouch);
      return;
    }
    final SP parsed;
    try {
      parsed = formatter.parse(value);
    } catch (Exception e) {
      isValid.set(false);
      if (shouldTouch) {
        // we failed to parse the value, but still treat this as touching the source property
        source.setTouched(true);
      }
      return;
    }
    isValid.set(true);
    source.set(parsed, shouldTouch);
  }

  @Override
  public void setInitialValue(DP value) {
    set(value, false);
  }

  @Override
  public void setDefaultValue(DP value) {
    try {
      source.setDefaultValue(formatter.parse(value));
    } catch (Exception e) {
      throw new RuntimeException("Default value cannot be parsed: " + value, e);
    }
  }

  @Override
  public DP getValue() {
    return get();
  }

  @Override
  public void setValue(DP value) {
    set(value);
  }

  @Override
  public void setValue(DP value, boolean fire) {
    set(value);
  }

  @Override
  public void setIfNull(DP value) {
    if (get() == null) {
      set(value);
    }
  }

  /**
   * Changes the message shown when the formatter cannot parse input.
   *
   * Note: ensure to change this only initially, and not after any potentially error messages have been fired.
   */
  public void setInvalidMessage(String message) {
    isValid.setMessage(message);
  }

  @Override
  public void reassess() {
    source.reassess();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void addRule(Rule<? super DP> rule) {
    source.addRule((Rule<? super SP>) rule); // hm, doesn't look right
  }

  @Override
  public boolean isTouched() {
    return source.isTouched();
  }

  @Override
  public void setTouched(boolean touched) {
    source.setTouched(touched);
  }

  @Override
  public boolean touch() {
    return source.touch();
  }

  @Override
  public boolean isValid() {
    return source.isValid();
  }

  @Override
  public Property<Boolean> valid() {
    return source.valid();
  }

  @Override
  public Property<Boolean> touched() {
    return source.touched();
  }

  @Override
  public Property<Boolean> changed() {
    return source.changed();
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    source.fireEvent(event);
  }

  @Override
  public <T extends Property<?>> T addDerived(T downstream, Object token, boolean touch) {
    return source.addDerived(downstream, token, touch);
  }

  @Override
  public <T extends Property<?>> T removeDerived(T downstream, Object token) {
    return source.removeDerived(downstream, token);
  }

  @Override
  public Property<DP> depends(Property<?>... upstream) {
    source.depends(upstream);
    return this;
  }

  @Override
  public HandlerRegistration addPropertyChangedHandler(final PropertyChangedHandler<DP> handler) {
    return source.addPropertyChangedHandler(new PropertyChangedHandler<SP>() {
      public void onPropertyChanged(PropertyChangedEvent<SP> event) {
        DP oldValue = event.getOldValue() == null ? null : formatter.format(event.getOldValue());
        DP newValue = event.getNewValue() == null ? null : formatter.format(event.getNewValue());
        handler.onPropertyChanged(new PropertyChangedEvent<DP>(FormattedProperty.this, oldValue, newValue));
      }
    });
  }

  @Override
  public HandlerRegistration nowAndOnChange(final PropertyValueHandler<DP> handler) {
    return source.nowAndOnChange(new PropertyValueHandler<SP>() {
      public void onValue(SP value) {
        DP newValue = value == null ? null : formatter.format(value);
        handler.onValue(newValue);
      }
    });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<DP> handler) {
    return source.addValueChangeHandler(new ValueChangeHandler<SP>() {
      public void onValueChange(ValueChangeEvent<SP> event) {
        DP newValue = event.getValue() == null ? null : formatter.format(event.getValue());
        // need an inner class because ValueChangedEvent's cstr is protected
        handler.onValueChange(new ValueChangeEvent<DP>(newValue) {
        });
      }
    });
  }

  @Override
  public HandlerRegistration addRuleTriggeredHandler(final RuleTriggeredHandler handler) {
    return source.addRuleTriggeredHandler(handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(final RuleUntriggeredHandler handler) {
    return source.addRuleUntriggeredHandler(handler);
  }

  @Override
  public String getName() {
    // keep the same name for when formatted properties are put into
    // a FormPresenter, they keep the same name
    return source.getName();
  }

  @Override
  public String getValueName() {
    return source.getValueName();
  }

  @Override
  public Map<Object, String> getErrors() {
    return source.getErrors();
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public Property<Boolean> is(DP value) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Property<Boolean> is(DP value, DP whenUnsetValue) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Property<Boolean> is(Property<DP> value) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Property<Boolean> is(Property<DP> value, DP whenUnsetValue) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean isRequired() {
    return source.isRequired();
  }

  @Override
  public void setRequired(boolean required) {
    source.setRequired(required);
  }

}
