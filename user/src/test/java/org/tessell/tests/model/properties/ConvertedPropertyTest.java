package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.tessell.model.properties.NewProperty.integerProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyConverter;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class ConvertedPropertyTest extends AbstractRuleTest {

  @Test
  public void get() {
    Property<String> p = integerProperty("i", 1).as(c);
    assertThat(p.get(), is("1-1"));
  }

  @Test
  public void testToString() {
    assertThat(integerProperty("i", 1).as(c).toString(), is("i 1-1"));
  }

  @Test
  public void testLambda() {
    assertThat(integerProperty("i", 1).as(i -> i + "-" + i).get(), is("1-1"));
  }

  @Test
  public void sourceChangingMakesTheAsValueChange() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.as(c);

    ChangeTracker tracker = new ChangeTracker();
    p.addPropertyChangedHandler(tracker);

    i.set(2);
    assertThat(tracker.values, contains("2-2"));
  }

  @Test
  public void touchSetsTouchOnTheOriginalProperty() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.as(c);
    p.touch();
    assertThat(i.isTouched(), is(true));
  }

  @Test(expected = IllegalStateException.class)
  public void setFails() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.as(c);
    assertThat(p.isReadOnly(), is(true));
    p.set("2");
  }

  @Test(expected = IllegalStateException.class)
  public void setInitialFails() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.as(c);
    p.setInitialValue("2");
    assertThat(i.getValue(), is(2));
    assertThat(i.isTouched(), is(false));
  }

  @Test
  public void testRecursion() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p1 = i.as(c);
    Property<String> p2 = p1.as(new PropertyConverter<String, String>() {
      public String to(String a) {
        return a + "-" + a;
      }
    });
    assertThat(p1.get(), is("1-1"));
    assertThat(p2.get(), is("1-1-1-1"));
  }

  @Test
  public void testCustomNullValue() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.as(new PropertyConverter<Integer, String>() {
      public String to(Integer a) {
        return a.toString();
      }

      public String nullValue() {
        return "NULL";
      }
    });
    assertThat(p.get(), is("1"));
    i.set(null);
    assertThat(p.get(), is("NULL"));
  }

  private final PropertyConverter<Integer, String> c = new PropertyConverter<Integer, String>() {
    public String to(Integer a) {
      return a + "-" + a;
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
