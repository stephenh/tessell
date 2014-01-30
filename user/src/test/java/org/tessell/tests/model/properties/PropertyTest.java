package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.tessell.model.properties.NewProperty.basicProperty;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.*;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.rules.Custom;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class PropertyTest extends AbstractRuleTest {

  @Test
  public void twoWayDerived() {
    final IntegerProperty a = integerProperty("a", 3);
    final IntegerProperty b = integerProperty("b", 2);
    listenTo(a);
    listenTo(b);
    a.touch();
    b.touch();

    a.addRule(new Custom("a must be greater than b", new DerivedValue<Boolean>() {
      public Boolean get() {
        return a.get() > b.get();
      }
    }));

    b.addRule(new Custom("b must be within 5 of a", new DerivedValue<Boolean>() {
      public Boolean get() {
        return Math.abs(a.get() - b.get()) <= 5;
      }
    }));

    assertMessages("");

    a.set(-10);
    assertMessages("a must be greater than b", "b must be within 5 of a");
  }

  @Test
  public void validationHappensBeforeChange() {
    final IntegerProperty a = integerProperty("a", 10);
    a.addRule(new Custom("a must be greater than 5", new DerivedValue<Boolean>() {
      public Boolean get() {
        return a.get() != null && a.get() > 5;
      }
    }));

    final Boolean[] wasInvalidOnChange = { null };
    a.addPropertyChangedHandler(new PropertyChangedHandler<Integer>() {
      public void onPropertyChanged(PropertyChangedEvent<Integer> event) {
        wasInvalidOnChange[0] = a.isValid() == false;
      }
    });

    a.set(1);
    assertThat(wasInvalidOnChange[0], is(true));
  }

  @Test
  public void validationOfDerivedValuesHappensBeforeChange() {
    final IntegerProperty a = integerProperty("a");
    final IntegerProperty b = integerProperty("b");
    b.addRule(new Custom("b must be greater than a", new DerivedValue<Boolean>() {
      public Boolean get() {
        return b.get() == null || a.get() == null || b.get() > a.get();
      }
    }));

    // set with good values
    a.set(1);
    b.set(2);

    final Boolean[] asWasInvalid = { null };
    a.addPropertyChangedHandler(new PropertyChangedHandler<Integer>() {
      public void onPropertyChanged(PropertyChangedEvent<Integer> event) {
        asWasInvalid[0] = b.isValid() == false;
      }
    });

    a.set(3);
    assertThat(asWasInvalid[0], is(true));
  }

  @Test
  public void wasValidIsSetBeforeRulesAreTriggered() {
    final IntegerProperty a = integerProperty("a").req();
    final Boolean[] wasValid = { null };
    a.addRuleTriggeredHandler(new RuleTriggeredHandler() {
      public void onTrigger(RuleTriggeredEvent event) {
        wasValid[0] = a.isValid() == true;
      }
    });
    a.touch();
    assertThat(wasValid[0], is(false));
  }

  @Test
  public void derivedWatchesGetValueMethod() {
    final IntegerProperty a = integerProperty("a");
    final BooleanProperty b = booleanProperty(new DerivedValue<Boolean>("not null") {
      public Boolean get() {
        return a.getValue() != null;
      }
    });
    CountChanges c = CountChanges.on(b);
    a.set(1);
    assertThat(c.changes, is(1));
  }

  @Test
  public void derivedWatchesIsValid() {
    final IntegerProperty a = integerProperty("a").req();
    final BooleanProperty b = booleanProperty(new DerivedValue<Boolean>("was valid") {
      public Boolean get() {
        return a.isValid() == true;
      }
    });
    CountChanges c = CountChanges.on(b);
    a.set(1);
    assertThat(c.changes, is(1));
  }

  @Test
  public void derivedWatchesValid() {
    final IntegerProperty a = integerProperty("a").req();
    final BooleanProperty b = booleanProperty(new DerivedValue<Boolean>("was valid") {
      public Boolean get() {
        return a.valid().get() == true;
      }
    });
    CountChanges c = CountChanges.on(b);
    a.set(1);
    assertThat(c.changes, is(1));
  }

  @Test
  public void derivedWatchesIsTouched() {
    final IntegerProperty a = integerProperty("a");
    final BooleanProperty b = booleanProperty(new DerivedValue<Boolean>("not null") {
      public Boolean get() {
        return a.isTouched();
      }
    });
    CountChanges c = CountChanges.on(b);
    a.set(1);
    assertThat(c.changes, is(1));
  }

  @Test
  public void setInitialLeavesPropertiesUnTouched() {
    final IntegerProperty a = integerProperty("a");
    a.setInitialValue(1);
    assertThat(a.isTouched(), is(false));
  }

  @Test
  public void setIfNullLeavesPropertiesUnTouched() {
    final IntegerProperty a = integerProperty("a");
    a.setIfNull(1);
    assertThat(a.isTouched(), is(false));
  }

  @Test
  public void setDefaultValueChangesValueRightAwayIfNull() {
    final IntegerProperty a = integerProperty("a");
    a.setDefaultValue(1);
    assertThat(a.get(), is(1));
    assertThat(a.isTouched(), is(false));
  }

  @Test
  public void setDefaultValueDoesNotChangeValueRightAwayIfNotNull() {
    final IntegerProperty a = integerProperty("a", 0);
    a.setDefaultValue(1);
    assertThat(a.get(), is(0));
    assertThat(a.isTouched(), is(false));
  }

  @Test
  public void setDefaultValueChangesNullWhenSetLater() {
    final IntegerProperty a = integerProperty("a", 0);
    a.setDefaultValue(1);
    a.set(null);
    assertThat(a.get(), is(1));
  }

  @Test
  public void setDefaultValueChangesNullWhenSetInitialLater() {
    final IntegerProperty a = integerProperty("a");
    a.setDefaultValue(1);
    a.setInitialValue(null);
    assertThat(a.get(), is(1));
  }

  @Test
  public void setDefaultValueFiresChange() {
    final BooleanProperty a = booleanProperty("a");
    CountChanges c = CountChanges.on(a);
    a.setDefaultValue(true);
    assertThat(c.changes, is(1));
    a.set(null);
    assertThat(c.changes, is(1));
  }

  @Test
  public void setDefaultValueWatchesForOutOfBandSets() {
    final SetValue<Boolean> b = new SetValue<Boolean>("b");
    final BooleanProperty p = booleanProperty(b);
    p.setDefaultValue(true);
    assertThat(b.get(), is(true));
    // now have b get changed out of band
    b.set(null);
    p.reassess();
    assertThat(p.get(), is(true));
    assertThat(b.get(), is(true));
  }

  @Test
  public void testIsValue() {
    final StringProperty s = stringProperty("s");
    final Property<Boolean> b = s.is("foo");
    CountChanges c = CountChanges.on(b);

    assertThat(b.getValue(), is(false));
    assertThat(b.isTouched(), is(false));

    s.set("foo");
    assertThat(b.getValue(), is(true));
    assertThat(s.get(), is("foo"));
    assertThat(c.changes, is(1));

    s.set("bar");
    assertThat(b.getValue(), is(false));
    assertThat(s.get(), is("bar"));
    assertThat(c.changes, is(2));

    b.setValue(true);
    assertThat(s.get(), is("foo"));
    assertThat(c.changes, is(3));

    b.setValue(false);
    assertThat(s.get(), is(nullValue()));
    assertThat(c.changes, is(4));
  }

  @Test
  public void testIsOther() {
    final StringProperty s1 = stringProperty("s1", "foo");
    final StringProperty s2 = stringProperty("s2", "bar");
    final Property<Boolean> b = s1.is(s2);
    CountChanges c = CountChanges.on(b);

    assertThat(b.getValue(), is(false));
    assertThat(b.isTouched(), is(false));

    s1.set("bar");
    assertThat(b.getValue(), is(true));
    assertThat(c.changes, is(1));
    assertThat(s1.get(), is("bar"));
    assertThat(s2.get(), is("bar"));

    s1.set("foo");
    assertThat(b.getValue(), is(false));
    assertThat(c.changes, is(2));
    assertThat(s1.get(), is("foo"));
    assertThat(s2.get(), is("bar"));

    s2.set("foo");
    assertThat(b.getValue(), is(true));
    assertThat(c.changes, is(3));
    assertThat(s1.get(), is("foo"));
    assertThat(s2.get(), is("foo"));

    b.setValue(false);
    assertThat(c.changes, is(4));
    assertThat(s1.get(), is(nullValue()));
    assertThat(s2.get(), is("foo"));

    b.setValue(true);
    assertThat(c.changes, is(5));
    assertThat(s1.get(), is("foo"));
    assertThat(s2.get(), is("foo"));
  }

  @Test
  public void testIsCondition() {
    final StringProperty s = stringProperty("s");
    final Property<Boolean> b = s.is(new Condition<String>() {
      public boolean evaluate(String value) {
        return value != null && value.length() > 3;
      }
    });
    CountChanges c = CountChanges.on(b);

    assertThat(b.getValue(), is(false));
    assertThat(b.isTouched(), is(false));

    s.set("food");
    assertThat(b.getValue(), is(true));
    assertThat(b.isTouched(), is(true));
    assertThat(c.changes, is(1));

    s.set("bar");
    assertThat(b.getValue(), is(false));
    assertThat(b.isTouched(), is(true));
    assertThat(c.changes, is(2));

    assertThat(b.isReadOnly(), is(true));
  }

  @Test
  public void testIsConditionWithMultipleUpstreamValues() {
    final StringProperty s1 = stringProperty("s1", "a");
    final StringProperty s2 = stringProperty("s2", "b");
    final Property<Boolean> oneIsBigger = s1.is(new Condition<String>() {
      public boolean evaluate(String value) {
        return value.length() > s2.get().length();
      }
    });
    CountChanges c = CountChanges.on(oneIsBigger);

    assertThat(oneIsBigger.getValue(), is(false));
    assertThat(oneIsBigger.isTouched(), is(false));

    // changing s1 updates the value
    s1.set("foo");
    assertThat(oneIsBigger.getValue(), is(true));
    assertThat(oneIsBigger.isTouched(), is(true));
    assertThat(c.changes, is(1));

    // changing s2 also updates the value
    s2.set("food");
    assertThat(oneIsBigger.getValue(), is(false));
    assertThat(oneIsBigger.isTouched(), is(true));
    assertThat(c.changes, is(2));
  }

  @Test
  public void testIsReadOnlyValue() {
    final StringProperty s = stringProperty(new DerivedValue<String>("s") {
      public String get() {
        return "s";
      }
    });
    final Property<Boolean> b = s.is("foo");
    assertThat(b.getValue(), is(false));
    // s is read-only, so this is a no-op
    b.setValue(true);
    assertThat(s.get(), is("s"));
    assertThat(b.getValue(), is(false));
  }

  @Test
  public void testIsReadOnlyProperty() {
    final StringProperty s = stringProperty(new DerivedValue<String>("s") {
      public String get() {
        return "s";
      }
    });
    final Property<Boolean> b = s.is(stringProperty("s2", "t"));
    assertThat(b.getValue(), is(false));
    // s is read-only, so this is a no-op
    b.setValue(true);
    assertThat(s.get(), is("s"));
    assertThat(b.getValue(), is(false));
  }

  @Test
  public void testAsString() {
    // a simple class that overrides toString
    class Foo {
      private final String value;

      public Foo(String value) {
        this.value = value;
      }

      @Override
      public String toString() {
        return value;
      }
    }

    final BasicProperty<Foo> foo = basicProperty("foo");
    final Property<String> s = foo.asString();
    final CountChanges count = CountChanges.on(s);

    assertThat(s.get(), is(nullValue()));
    foo.set(new Foo("bar"));
    assertThat(s.get(), is("bar"));
    assertThat(count.changes, is(1));
  }

  @Test
  public void canAddTemporaryErrors() {
    final BasicProperty<String> s = basicProperty("s");
    s.setTouched(true);
    listenTo(s);
    s.setTemporaryError("Something bad");
    assertMessages("Something bad");
  }

  @Test
  public void canClearTemporary() {
    final BasicProperty<String> s = basicProperty("s");
    s.setTouched(true);
    listenTo(s);
    s.setTemporaryError("Something bad");
    s.clearTemporaryError();
    assertNoMessages();
  }

  @Test
  public void canChangeTemporaryError() {
    final BasicProperty<String> s = basicProperty("s");
    s.setTouched(true);
    listenTo(s);
    s.setTemporaryError("Something bad 1");
    s.setTemporaryError("Something bad 2");
    assertMessages("Something bad 2");
  }

  @Test
  public void temporaryErrorTakesPrecedenceOverOtherRules() {
    final BasicProperty<String> s = basicProperty("s");
    s.req();
    listenTo(s);
    s.setTouched(true);
    assertMessages("S is required");
    s.setTemporaryError("Something bad");
    assertMessages("Something bad");
  }

  @Test
  public void otherRulesComeBackWhenTemporaryErrorIsCleared() {
    final BasicProperty<String> s = basicProperty("s");
    s.req();
    listenTo(s);
    s.setTouched(true);
    assertMessages("S is required");
    s.setTemporaryError("Something bad");
    s.clearTemporaryError();
    assertMessages("S is required");
  }

  @Test
  public void temporaryErrorIsClearedByTouching() {
    final BasicProperty<String> s = basicProperty("s");
    listenTo(s);
    s.setTouched(true);
    s.setTemporaryError("Something bad");
    assertMessages("Something bad");
    s.setTouched(true);
    assertNoMessages();
  }

  @Test
  public void orIfNull() {
    final Property<String> a = basicProperty("s");
    final Property<String> b = a.orIfNull("unset");
    assertThat(b.get(), is("unset"));
    a.set("foo");
    assertThat(b.get(), is("foo"));
    a.set(null);
    assertThat(b.get(), is("unset"));
  }

  @Test
  public void validFiresChangeEvents() {
    final BasicProperty<String> s = basicProperty("s");
    CountChanges c = CountChanges.on(s.valid());
    assertThat(c.changes, is(0));
    s.req();
    assertThat(c.changes, is(1));
    s.set("asdf");
    assertThat(c.changes, is(2));
  }

  @Test
  public void isSet() {
    final BasicProperty<String> s = basicProperty("s");
    final Property<Boolean> set = s.isSet();
    CountChanges c = CountChanges.on(set);

    s.set("asdf");
    assertThat(c.changes, is(1));
    assertThat(set.get(), is(true));

    s.set(null);
    assertThat(c.changes, is(2));
    assertThat(set.get(), is(false));

    assertThat(set.isReadOnly(), is(true));
  }

  private static class CountChanges {
    private static <T> CountChanges on(Property<T> source) {
      final CountChanges c = new CountChanges();
      source.addPropertyChangedHandler(new PropertyChangedHandler<T>() {
        public void onPropertyChanged(PropertyChangedEvent<T> event) {
          c.changes++;
        }
      });
      return c;
    }

    private int changes;
  }

}
