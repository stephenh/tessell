package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.rules.Range;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.widgets.StubHasValue;
import org.junit.Before;
import org.junit.Test;

public class DerivedRulesTest extends AbstractRuleTest {

  public static class Foo {
    public StringProperty name = stringProperty("name").max(10);
  }

  private final Foo f = new Foo();

  @Before
  public void listenToName() {
    listenTo(f.name);
  }

  @Test
  public void derivedFiresChangedWhenSourceDoes() {
    final CountChanged<Integer> count = new CountChanged<Integer>();
    f.name.remaining().addPropertyChangedHandler(count);
    assertThat(count.count, is(0));

    f.name.set("foo");
    assertThat(count.count, is(1));
  }

  @Test
  public void derivedItselfDoesNotFireChangedIfPropertyAlreadySet() {
    f.name.set("foo");
    final CountChanged<Integer> count = new CountChanged<Integer>();
    f.name.remaining().addPropertyChangedHandler(count);
    assertThat(count.count, is(0));
  }

  @Test
  public void binderDoesFireChangedIfPropertyAlreadySet() {
    f.name.set("foo");
    final StubHasValue<Integer> value = new StubHasValue<Integer>();
    Binder b = new Binder();
    b.bind(f.name.remaining()).to(value);
    assertThat(value.getValue(), is(7));
  }

  @Test
  public void derivedDoesNotReFireIfValueHasNotChanged() {
    final CountChanged<Integer> count = new CountChanged<Integer>();
    f.name.remaining().addPropertyChangedHandler(count);
    f.name.set("foo");
    assertThat(count.count, is(1));
    f.name.set("bar");
    assertThat(count.count, is(1)); // still 1
  }

  @Test(expected = IllegalStateException.class)
  public void derivedPropertiesCannotBeSet() {
    f.name.remaining().set(10);
  }

  @Test
  public void derivedCanHaveRules() {
    // listen to the derived property
    final IntegerProperty length = f.name.length();
    new Range(length, "invalid range", 0, 5);
    listenTo(length);

    f.name.set("123456");
    assertMessages("invalid range");

    f.name.set("12345");
    assertMessages("");
  }

  @Test
  public void userDerivedWillGetChangeEvents() {
    final StringProperty parent = stringProperty("parent", "");
    final Property<Boolean> logic = booleanProperty(new DerivedValue<Boolean>() {
      public Boolean get() {
        return parent.get().length() > 0;
      }
    }).depends(parent);

    final CountChanged<Boolean> c = new CountChanged<Boolean>();
    logic.addPropertyChangedHandler(c);

    parent.set("asdf");
    assertThat(c.count, is(1));
  }

  private class CountChanged<P> implements PropertyChangedHandler<P> {
    private int count;

    @Override
    public void onPropertyChanged(final PropertyChangedEvent<P> event) {
      count++;
    }
  }

}
