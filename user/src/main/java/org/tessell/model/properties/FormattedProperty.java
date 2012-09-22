package org.tessell.model.properties;

import java.util.Map;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.validation.Valid;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.validation.rules.Static;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Converts a source property to/from another type, using a {@link PropertyFormatter}. 
 *
 * @param <SP> the source property type
 * @param <DP> the destination property type
 */
public class FormattedProperty<DP, SP> implements Property<DP> {

  private final Property<SP> source;
  private final PropertyFormatter<SP, DP> formatter;
  private final Static isValid;

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter) {
    this(source, formatter, null);
  }

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter, final String message) {
    this.source = source;
    this.formatter = formatter;
    // note, we currently fire the error against our source property, so that people listening for errors
    // to it will see them. it might make more sense to fire against us first, then our source property.
    isValid = new Static((message == null) ? source.getName() + " is invalid" : message) {
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
  public DP get() {
    SP value = source.get();
    return (value == null) ? null : formatter.format(value);
  }

  @Override
  public void set(DP value) {
    // null and "" are special
    if (value == null || "".equals(value)) {
      isValid.set(true);
      source.set(null);
      return;
    }
    final SP parsed;
    try {
      parsed = formatter.parse(value);
    } catch (Exception e) {
      isValid.set(false);
      // we failed to parse the value, but still treat this as touching the source property
      source.setTouched(true);
      return;
    }
    isValid.set(true);
    source.set(parsed);
  }

  @Override
  public void setInitialValue(DP value) {
    // null and "" are special
    if (value == null || "".equals(value)) {
      isValid.set(true);
      source.setInitialValue(null);
      return;
    }
    final SP parsed;
    try {
      parsed = formatter.parse(value);
    } catch (Exception e) {
      isValid.set(false);
      return;
    }
    isValid.set(true);
    source.setInitialValue(parsed);
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

  public String valueName() {
    return "formatted " + source.getName();
  }

  @Override
  public HandlerRegistration addRuleTriggeredHandler(RuleTriggeredHandler handler) {
    return source.addRuleTriggeredHandler(handler);
  }

  @Override
  public HandlerRegistration addRuleUntriggeredHandler(RuleUntriggeredHandler handler) {
    return source.addRuleUntriggeredHandler(handler);
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
  public Valid touch() {
    return source.touch();
  }

  @Override
  public Valid wasValid() {
    return source.wasValid();
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    source.fireEvent(event);
  }

  @Override
  public <T extends Property<?>> T addDerived(T downstream) {
    return source.addDerived(downstream);
  }

  @Override
  public <T extends Property<?>> T addDerived(T downstream, Object token, boolean touch) {
    return source.addDerived(downstream, token, touch);
  }

  @Override
  public <T extends Property<?>> T removeDerived(T downstream) {
    return source.removeDerived(downstream);
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
  public String getName() {
    // keep the same name for when formatted properties are put into
    // a FormPresenter, they keep the same name
    return source.getName();
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
  public <T1> Property<T1> formatted(PropertyFormatter<DP, T1> formatter) {
    throw new UnsupportedOperationException("Not implemented");
  }

}
