package org.tessell.model.properties;

import java.util.Map;

import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.values.Value;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasValue;

public interface Property<P> extends HasHandlers, HasRuleTriggers, Value<P>, HasValue<P> {

  /** @return the current property value. */
  P get();

  /** Sets {@code value}, with marking touched, and firing events. */
  void set(P value);

  /** Sets {@code value} and fires events, without marking touched. */
  void setInitialValue(P value);

  /** Sets {@code value} whenever the property's value becomes {@code null}. */
  void setDefaultValue(P value);

  /** Sets {@code value} only if we're current null, without marking touched. */
  void setIfNull(P value);

  /**
   * Re-examines the value to see if it's changed/invalid.
   *
   * This shouldn't need to be called directly unless there have been out-of-band
   * changes (e.g. the underlying value changed without Tessell knowing about it).
   */
  void reassess();

  void addRule(Rule<? super P> rule);

  /** @return whether the user has tried to set this property yet. */
  boolean isTouched();

  /** Sets whether this property has been touched by the user. */
  void setTouched(boolean touched);

  /** @return whether this property is required. */
  boolean isRequired();

  /** Sets whether this property is required. */
  void setRequired(boolean required);

  /** Fluent method for marking a property as touched. */
  boolean touch();

  /** @return whether this property is valid. */
  boolean isValid();

  /** @return whether this property is valid, as a property. */
  Property<Boolean> valid();

  /** Adds {@code downstream} as a derivative of us. */
  <T extends Property<?>> T addDerived(T downstream);

  /** Adds {@code downstream} as a derivative of us. */
  <T extends Property<?>> T addDerived(T downstream, Object token, boolean touch);

  /** Removes {@code downstream} as a derivative of us. */
  <T extends Property<?>> T removeDerived(T downstream);

  <T extends Property<?>> T removeDerived(T downstream, Object token);

  /** Adds us as a derivative of {@code upstream} properties. */
  Property<P> depends(Property<?>... upstream);

  /** Adds {@code handler} to be called on property change. */
  HandlerRegistration addPropertyChangedHandler(PropertyChangedHandler<P> handler);

  /** Adds {@code handler} to be ran immediately and also on property change. */
  HandlerRegistration nowAndOnChange(PropertyValueHandler<P> handler);

  /** @return the name of the property. */
  String getName();

  /** @return a map of outstanding validation errors. */
  Map<Object, String> getErrors();

  /** @return a derived property that is formatted/parsed with {@code formatter}. */
  <T1> FormattedProperty<T1, P> formatted(PropertyFormatter<P, T1> formatter);

  /** @return a derived property that is formatted/parsed with {@code formatter}. */
  <T1> FormattedProperty<T1, P> formatted(String invalidMessage, PropertyFormatter<P, T1> formatter);

  /** @return a derived property that is converted with {@code converter}. */
  <T1> Property<T1> as(PropertyConverter<P, T1> converter);

  Property<String> asString();

  /** @return a two-way property that is true/value when {@code this} equals {@code value}. */
  Property<Boolean> is(P value);

  /** @return a two-way property that is true/value when {@code this} equals {@code value}. */
  Property<Boolean> is(P value, P whenUnsetValue);

  /** @return a two-way property that is true/value when {@code this} equals {@code other}. */
  Property<Boolean> is(Property<P> other);

  /** @return a two-way property that is true/value when {@code this} equals {@code other}. */
  Property<Boolean> is(Property<P> other, P whenUnsetValue);

  /** @return a one-way property that is true when {@code this} passes {@code condition}. */
  Property<Boolean> is(Condition<P> condition);

  /** @return a derived property will be {@code value} if this property is {@code null}. */
  Property<P> orIfNull(P value);

  /** @return a read-only property that is true/false when {@code this} is set. */
  Property<Boolean> isSet();
}
