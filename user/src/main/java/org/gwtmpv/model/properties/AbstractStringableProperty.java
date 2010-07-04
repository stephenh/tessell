package org.gwtmpv.model.properties;

import org.gwtmpv.model.validation.rules.Static;
import org.gwtmpv.model.values.Value;

/** Abstract property for properties that can be strings. */
public abstract class AbstractStringableProperty<T, U extends AbstractStringableProperty<T, U>> extends
    AbstractProperty<T, U> implements StringableProperty {

  private final Static fromStringRule;

  public AbstractStringableProperty(final Value<T> value) {
    super(value);
    fromStringRule = new Static(this, getName() + " is invalid");
    fromStringRule.set(true); // start out off
  }

  @Override
  public String getAsString() {
    if (get() == null) {
      return null;
    }
    return getAsNonNullString();
  }

  @Override
  public void setAsString(final String value) {
    if (value == null || "".equals(value)) {
      set(null);
      return;
    }
    final T newValue;
    try {
      newValue = fromFailableString(value);
    } catch (final Exception e) {
      fromStringRule.set(false);
      setTouched(true); // even if unchanged, treat this as touching
      return;
    }
    set(newValue);
  }

  protected abstract String getAsNonNullString();

  protected abstract T fromFailableString(String value) throws Exception;

  @Override
  public void set(final T value) {
    fromStringRule.set(true);
    super.set(value);
  }

  @Override
  public void setInitial(final T value) {
    fromStringRule.set(true);
    super.setInitial(value);
  }

  @Override
  public void setFromStringErrorMessage(final String fromStringErrorMessage) {
    fromStringRule.setMessage(fromStringErrorMessage);
  }

}
