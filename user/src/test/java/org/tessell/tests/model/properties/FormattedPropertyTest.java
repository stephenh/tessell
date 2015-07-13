package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.tessell.model.properties.NewProperty.integerProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.model.dsl.Binder;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyFormatter;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class FormattedPropertyTest extends AbstractRuleTest {

  @Test
  public void get() {
    Property<String> p = integerProperty("i", 1).formatted(intToString);
    assertThat(p.get(), is("1"));
  }

  @Test
  public void testToString() {
    assertThat(integerProperty("i", 1).formatted(intToString).toString(), is("i 1"));
  }

  @Test
  public void setStringToInt() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(intToString);
    p.set("2");
    assertThat(i.get(), is(2));
  }

  @Test
  public void setBadString() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(intToString);
    p.set("a");
    assertMessages("I is invalid");
  }

  @Test
  public void setBadStringThatIsRequired() {
    IntegerProperty i = integerProperty("i", 1).req();
    listenTo(i);

    Property<String> p = i.formatted(intToString);
    p.set("a");
    assertMessages("I is invalid");
    // we keep the old value, because setting to null would send up
    // resetting the user's last-invalid attempt in the UI
    assertThat(i.get(), is(1));

    i.set(2);
    assertNoMessages();

    p.set(null);
    assertMessages("I is required");
  }

  @Test
  public void setBadStringThatIsBoundToATextBox() {
    IntegerProperty i = integerProperty("i", 1).req();
    Binder binder = new Binder();
    StubTextBox box = new StubTextBox();
    binder.bind(i.asString()).to(box);
    listenTo(i);

    box.type("a");
    assertThat(box.getText(), is("a"));
    assertMessages("I must be an integer");
    assertThat(i.get(), is(1));
    assertThat(box.getText(), is("a"));

    i.set(2);
    assertNoMessages();

    box.type("");
    assertMessages("I is required");
  }

  @Test
  public void setNull() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    i.asString().set(null);
    assertThat(i.get(), is(nullValue()));
    assertThat(i.isValid(), is(true));
  }

  @Test
  public void setEmptyString() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    i.asString().set("");
    assertThat(i.get(), is(nullValue()));
    assertThat(i.isValid(), is(true));
  }

  @Test
  public void setThatFailsThenIsCorrected() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(intToString);
    p.set("a");
    assertMessages("I is invalid");

    p.set("2");
    assertMessages("");
  }

  @Test
  public void setThatFailsThenIsCorrectedInOriginalProperty() {
    IntegerProperty i = integerProperty("i", 1);
    listenTo(i);

    Property<String> p = i.formatted(intToString);
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
    assertMessages("I must be an integer");
  }

  @Test
  public void sourceChangingMakesTheFormattedValueChange() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(intToString);

    ChangeTracker tracker = new ChangeTracker();
    p.addPropertyChangedHandler(tracker);

    i.set(2);
    assertThat(tracker.values, contains("2"));
  }

  @Test
  public void setInitialLeavesSourcePropertyUntouched() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(intToString);
    p.setInitialValue("2");
    assertThat(i.getValue(), is(2));
    assertThat(i.isTouched(), is(false));
  }

  @Test
  public void setInitialLeavesSourcePropertyUntouchedEvenOnErrors() {
    // note that this behavior seems right; but it's not
    // driven from real world usage
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(intToString);
    p.setInitialValue("blah");
    assertThat(i.getValue(), is(1));
    assertThat(i.isTouched(), is(false));
  }

  @Test
  public void testRecursion() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p1 = i.formatted(intToString);
    Property<Integer> p2 = p1.formatted(stringToInt);

    assertThat(p1.get(), is("1"));
    assertThat(p2.get().intValue(), is(1));

    p2.set(2);
    assertThat(p1.get(), is("2"));
    assertThat(i.get().intValue(), is(2));
  }

  @Test
  public void testCustomNullValue() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> s = i.formatted(new PropertyFormatter<Integer, String>() {
      public String format(Integer a) {
        return a.toString();
      }

      public Integer parse(String b) throws Exception {
        return new Integer(b);
      }

      public String nullValue() {
        return "NULL";
      }
    });

    i.set(null);
    assertThat(s.get(), is("NULL"));
  }

  @Test
  public void testSettingInitialValueWhenBound() {
    IntegerProperty i = integerProperty("i");
    Property<String> s = i.asString();
    StubTextBox textBox = new StubTextBox();

    Binder b = new Binder();
    b.bind(s).to(textBox);

    i.setInitialValue(1);
    assertThat(i.isTouched(), is(false));
  }

  @Test
  public void testSettingValueWithTouchIsFalseWhenBound() {
    IntegerProperty i = integerProperty("i");
    Property<String> s = i.asString();
    StubTextBox textBox = new StubTextBox();

    Binder b = new Binder();
    b.bind(s).to(textBox);

    i.set(1, false);
    assertThat(i.isTouched(), is(false));
  }

  private final PropertyFormatter<Integer, String> intToString = new PropertyFormatter<Integer, String>() {
    @Override
    public String format(Integer a) {
      return a.toString();
    }

    @Override
    public Integer parse(String b) {
      return Integer.parseInt(b);
    }
  };

  private final PropertyFormatter<String, Integer> stringToInt = new PropertyFormatter<String, Integer>() {
    @Override
    public Integer format(String a) {
      return new Integer(a);
    }

    @Override
    public String parse(Integer i) {
      return i.toString();
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
