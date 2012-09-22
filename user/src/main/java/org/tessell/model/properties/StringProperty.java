package org.tessell.model.properties;

import static org.tessell.model.properties.NewProperty.integerProperty;

import org.tessell.model.validation.rules.Length;
import org.tessell.model.validation.rules.Regex;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.Value;

public class StringProperty extends AbstractProperty<String, StringProperty> {

  private Integer maxLength;

  public StringProperty(final Value<String> value) {
    super(value);
  }

  public StringProperty len(int minLength, int maxLength) {
    addRule(new Length(getName() + " must be between " + minLength + " and " + maxLength, minLength, maxLength));
    this.maxLength = maxLength;
    return this;
  }

  public StringProperty max(final int maxLength) {
    addRule(new Length(getName() + " must be less than " + maxLength, 0, maxLength));
    this.maxLength = maxLength;
    return this;
  }

  public StringProperty regex(final String regex, final String message) {
    addRule(new Regex(message, regex));
    return this;
  }

  public StringProperty numeric() {
    addRule(new Regex(getName() + " must be numeric", Regex.NUMERIC));
    return this;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public Integer getRemaining() {
    return get() == null ? maxLength : maxLength - get().length();
  }

  public Integer getLength() {
    return get() == null ? null : get().length();
  }

  public IntegerProperty maxLength() {
    return addDerived(integerProperty(new DerivedValue<Integer>() {
      public Integer get() {
        return getMaxLength();
      }
    }));
  }

  public IntegerProperty length() {
    return addDerived(integerProperty(new DerivedValue<Integer>() {
      public Integer get() {
        return getLength();
      }
    }));
  }

  public IntegerProperty remaining() {
    return addDerived(integerProperty(new DerivedValue<Integer>() {
      public Integer get() {
        return getRemaining();
      }
    }));
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  protected StringProperty getThis() {
    return this;
  }

}
