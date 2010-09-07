package org.gwtmpv.model.properties;

import org.gwtmpv.model.events.PropertyChangedEvent.PropertyChangedHandler;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.rules.Rule;
import org.gwtmpv.model.values.Value;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface Property<P> extends HasRuleTriggers {

  P get();

  void set(P value);

  void setInitial(P value);

  void reassess();

  void addRule(Rule rule);

  boolean isTouched();

  void setTouched(boolean touched);

  /** @return whether this property was invalid, does not rerun validation. */
  Valid wasValid();

  // for rules to fire events against our handlers
  void fireEvent(GwtEvent<?> event);

  <T extends Property<?>> T addDerived(final T other);

  /** Adds us as derivatives of {@code upstream} properties. */
  Property<P> depends(Property<?>... upstream);

  HandlerRegistration addPropertyChangedHandler(PropertyChangedHandler<P> handler);

  Value<P> getValue();

  String getName();

  void pullInitial();

}
