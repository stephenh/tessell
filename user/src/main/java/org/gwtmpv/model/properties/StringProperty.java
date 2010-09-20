package org.gwtmpv.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;

import org.gwtmpv.model.validation.rules.Regex;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.Value;

public class StringProperty extends AbstractProperty<String, StringProperty> {

  private Integer maxLength;

  public StringProperty(final Value<String> value) {
    super(value);
  }

  public StringProperty max(final int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public StringProperty numeric() {
    new Regex(this, getName() + " must be numeric", Regex.NUMERIC);
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

  @Override
  protected StringProperty getThis() {
    return this;
  }

}
