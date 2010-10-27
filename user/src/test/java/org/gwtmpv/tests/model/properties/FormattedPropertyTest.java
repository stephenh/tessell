package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyFormatter;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.tests.model.validation.rules.AbstractRuleTest;
import org.junit.Test;

public class FormattedPropertyTest extends AbstractRuleTest {

  @Test
  public void get() {
    Property<String> p = integerProperty("i", 1).formatted(f);
    assertThat(p.get(), is("1-1"));
  }

  @Test
  public void setStringToInt() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(f);
    p.set("2");
    assertThat(i.get(), is(2));
  }

  @Test
  public void setBadString() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(f);
    p.set("a");
    assertMessages("I is invalid");
  }

  @Test
  public void setNull() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    i.asString().set(null);
    assertThat(i.get(), is(nullValue()));
    assertThat(i.wasValid(), is(Valid.YES));
  }

  @Test
  public void setEmptyString() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    i.asString().set("");
    assertThat(i.get(), is(nullValue()));
    assertThat(i.wasValid(), is(Valid.YES));
  }

  @Test
  public void setThatFailsThenIsCorrected() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(f);
    p.set("a");
    assertMessages("I is invalid");

    p.set("2");
    assertMessages("");
  }

  @Test
  public void setThatFailsThenIsCorrectedInOriginalProperty() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(f);
    p.set("a");
    assertMessages("I is invalid");

    i.set(2);
    assertMessages("");
  }

  @Test
  public void setThatFailsHasInvalidMessageTakePrecedenceOverRequired() {
    IntegerProperty i = integerProperty("i", null).req();
    listenTo(i);

    i.asString().set("asdf");
    assertMessages("I is invalid");
  }

  @Test
  public void sourceChangingMakesTheFormattedValueChange() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(f);

    ChangeTracker tracker = new ChangeTracker();
    p.addPropertyChangedHandler(tracker);

    i.set(2);
    assertThat(tracker.values, contains("2-2"));
  }

  private final PropertyFormatter<Integer, String> f = new PropertyFormatter<Integer, String>() {
    @Override
    public String format(Integer a) {
      return a + "-" + a;
    }

    @Override
    public Integer parse(String b) {
      return Integer.parseInt(b);
    }
  };

  private class ChangeTracker implements PropertyChangedHandler<String> {
    private final List<String> values = new ArrayList<String>();

    @Override
    public void onPropertyChanged(PropertyChangedEvent<String> event) {
      values.add(event.getProperty().get());
    }
  }

}
