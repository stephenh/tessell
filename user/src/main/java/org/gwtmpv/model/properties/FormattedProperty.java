package org.gwtmpv.model.properties;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;
import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.model.validation.rules.Static;

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
    isValid = new Static(source, (message == null) ? source.getName() + " is invalid" : message) {
      @Override
      public boolean isImportant() {
        return true;
      }
    };
    // ((DelegatedValue<DP>) getValue()).setDelegate(this);
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
  public void addRule(Rule rule) {
    source.addRule(rule);
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
  public Property<DP> depends(Property<?>... upstream) {
    source.depends(upstream);
    return this;
  }

  @Override
  public HandlerRegistration addPropertyChangedHandler(final PropertyChangedHandler<DP> handler) {
    return source.addPropertyChangedHandler(new PropertyChangedHandler<SP>() {
      public void onPropertyChanged(PropertyChangedEvent<SP> event) {
        handler.onPropertyChanged(new PropertyChangedEvent<DP>(FormattedProperty.this));
      }
    });
  }

  @Override
  public String getName() {
    return "formatted " + source.getName();
  }

}
