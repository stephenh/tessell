package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.rules.Custom;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class PropertyTest extends AbstractRuleTest {

  @Test
  public void twoWayDerived() {
    final IntegerProperty a = integerProperty("a", 1);
    final IntegerProperty b = integerProperty("b", 2);
    listenTo(a);
    listenTo(b);

    new Custom(a, "a must be greater than b", new DerivedValue<Boolean>() {
      public Boolean get() {
        return a.get() > b.get();
      }
    });
    a.addDerived(b);

    new Custom(b, "b must be within 5 of a", new DerivedValue<Boolean>() {
      public Boolean get() {
        return Math.abs(a.get() - b.get()) <= 5;
      }
    });
    b.addDerived(a);

    a.reassess();
    assertMessages("");

    a.set(-10);
    assertMessages("b must be within 5 of a", "a must be greater than b");
  }

  @Test
  public void validationHappensBeforeChange() {
    final IntegerProperty a = integerProperty("a", 10);
    new Custom(a, "a must be greater than 5", new DerivedValue<Boolean>() {
      public Boolean get() {
        return a.get() != null && a.get() > 5;
      }
    });

    final Boolean[] wasInvalidOnChange = { null };
    a.addPropertyChangedHandler(new PropertyChangedHandler<Integer>() {
      public void onPropertyChanged(PropertyChangedEvent<Integer> event) {
        wasInvalidOnChange[0] = a.wasValid() == Valid.NO;
      }
    });

    a.set(1);
    assertThat(wasInvalidOnChange[0], is(true));
  }

}
