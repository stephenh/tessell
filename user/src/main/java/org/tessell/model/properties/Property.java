package org.tessell.model.properties;

import java.util.Map;

import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.validation.Valid;
import org.tessell.model.validation.rules.Rule;
import org.tessell.model.values.Value;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasValue;

public interface Property<P> extends HasHandlers, HasRuleTriggers, Value<P>, HasValue<P> {

  P get();

  void set(P value);

  void setInitialValue(P value);

  void setIfNull(P value);

  void reassess();

  void addRule(Rule<? super P> rule);

  boolean isTouched();

  void setTouched(boolean touched);

  Valid touch();

  /** @return whether this property was invalid, does not rerun validation. */
  Valid wasValid();

  /** Adds {@code downstream} as a derivative of us. */
  <T extends Property<?>> T addDerived(final T downstream);

  /** Adds {@code downstream} as a derivative of us. */
  <T extends Property<?>> T addDerived(final T downstream, Object token, boolean touch);

  /** Removes {@code downstream} as a derivative of us. */
  <T extends Property<?>> T removeDerived(final T downstream);

  <T extends Property<?>> T removeDerived(final T downstream, Object token);

  /** Adds us as a derivative of {@code upstream} properties. */
  Property<P> depends(Property<?>... upstream);

  HandlerRegistration addPropertyChangedHandler(PropertyChangedHandler<P> handler);

  String getName();

  Map<Object, String> getErrors();

  <T1> Property<T1> formatted(final PropertyFormatter<P, T1> formatter);

  <T1> Property<T1> formatted(String invalidMessage, final PropertyFormatter<P, T1> formatter);

}
